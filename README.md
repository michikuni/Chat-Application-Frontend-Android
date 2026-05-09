# MyApplication

Android chat / social app (Kotlin + Jetpack Compose) hỗ trợ chuyển đổi giữa **REST backend** và **Cloud Firestore** thông qua Firebase Remote Config.

---

## 1. Stack chính

| Mảng | Công nghệ |
| --- | --- |
| UI | Jetpack Compose, Material 3, Navigation Compose |
| DI | Hilt (KSP) |
| Network | Retrofit + OkHttp + Gson |
| Realtime / Push | Firebase Cloud Messaging |
| Backend toggle | Firebase Remote Config (`use_firestore`) |
| Cloud DB | Cloud Firestore |
| Async | Kotlin Coroutines, Flow / StateFlow |
| Image | Coil |
| Storage local | DataStore + SharedPreferences |

---

## 2. Kiến trúc (Clean Architecture)

```
com.company.myapplication
├── core/                            # Hạ tầng dùng chung
│   ├── config/                      # Remote Config + Backend selector
│   ├── error/                       # Exception domain (FeatureUnavailableException)
│   ├── network/                     # AuthInterceptor (Hilt-aware)
│   └── util/                        # ToastHelper (toast "không khả dụng")
│
├── data/
│   ├── api/                         # Retrofit interfaces
│   ├── model/                       # DTO/POJO (request, response, entity)
│   ├── remote/datasource/           # Gọi REST backend qua Retrofit
│   ├── firestore/                   # Firestore collections + datasource
│   │   └── datasource/              # Gọi Cloud Firestore
│   └── repository/                  # Repository impl (dispatcher REST/Firestore)
│
├── domain/
│   └── repository/                  # Interface Repository (contract của Domain layer)
│
├── di/                              # Hilt modules
│   ├── NetworkModule.kt             # Retrofit, OkHttp, Api interfaces
│   ├── FirebaseModule.kt            # Firestore, RemoteConfig
│   └── RepositoryModule.kt          # Bind interface ↔ implementation
│
├── presentation (giữ nguyên ui/, viewmodel/, navigation/)
│   ├── viewmodel/                   # @HiltViewModel
│   ├── ui/                          # Compose screens
│   └── navigation/                  # AppNavigation (hiltViewModel)
│
├── fcm/                             # Firebase Messaging service
├── util/                            # SharedPreferences, helpers
├── MainActivity.kt                  # @AndroidEntryPoint
└── MyApplication.kt                 # @HiltAndroidApp + bootstrap RemoteConfig
```

### Luồng dữ liệu
```
Compose UI ──► ViewModel (@HiltViewModel)
                   │
                   ▼
        Domain Repository (interface)
                   │
                   ▼
        RepositoryImpl (data) ──► BackendSelector
                                       │
                  ┌────────────────────┴─────────────────────┐
                  ▼                                          ▼
         RemoteDataSource                          FirestoreDataSource
         (Retrofit + REST)                         (Cloud Firestore)
```

`BackendSelector` đọc cờ `use_firestore` từ `RemoteConfigManager` để định tuyến mỗi lời gọi.

---

## 3. Firebase Remote Config

### 3.1. Tham số

| Key | Type | Default | Ý nghĩa |
| --- | --- | --- | --- |
| `use_firestore` | Boolean | `false` | `true` ⇒ dùng Firestore, `false` ⇒ dùng REST backend |

### 3.2. Tạo trên Firebase Console
1. Mở Firebase Console → Project `chat-application-dd146` → **Remote Config**.
2. Bấm **Add parameter**.
3. `Parameter name` = `use_firestore`, `Data type` = Boolean, `Default value` = `false`.
4. **Publish changes**.

### 3.3. Triển khai trong app
- `MyApplication.onCreate()` gọi `remoteConfigManager.fetchAndActivate()` mỗi lần khởi động.
- `RemoteConfigManager` set `defaults` = `false`, `minimumFetchInterval` = 3600s.
- `BackendSelector.current()` trả về `BackendType.REMOTE` hoặc `BackendType.FIRESTORE`.

### 3.4. Đổi backend không cần build lại
1. Vào Remote Config → đổi `use_firestore` thành `true` (hoặc `false`).
2. Bấm **Publish**.
3. App fetch lần khởi động tiếp theo → backend mới được áp dụng (cache TTL = 1 giờ; có thể giảm ở `RemoteConfigManager.DEFAULT_FETCH_INTERVAL_SECONDS` để test nhanh).

---

## 4. Tính năng không khả dụng trên Firestore

Firestore **không lưu trữ file nhị phân**. Khi `use_firestore = true`, các tính năng phụ thuộc upload file/multipart sẽ hiển thị toast:

> **"Chức năng hiện tại không khả dụng"**

| Tính năng | Backend REST | Firestore |
| --- | --- | --- |
| Đăng ký / Đăng nhập | ✅ | ✅ (Firebase Authentication email/password) |
| Lấy thông tin user | ✅ | ✅ |
| Bạn bè (gửi/nhận/duyệt/huỷ) | ✅ | ✅ |
| Danh sách / tin nhắn hội thoại | ✅ | ✅ |
| Gửi tin nhắn văn bản | ✅ | ✅ |
| Đổi theme hội thoại | ✅ | ✅ |
| Đăng feed (chỉ text) | ✅ | ✅ |
| Lấy danh sách feed | ✅ | ✅ |
| **Upload avatar** | ✅ | ❌ Toast |
| **Tạo nhóm (kèm avatar)** | ✅ | ❌ Toast |
| **Gửi media trong chat** | ✅ | ❌ Toast |
| **Đăng feed có media** | ✅ | ❌ Toast |
| FCM push | ✅ (lưu token qua REST) | ✅ (lưu token vào collection `fcm_tokens`) |

---

## 5. Cấu trúc Firestore

```
users/{userId}
  id: Long
  name: String
  username: String
  email: String
  password: String   (demo - không khuyến nghị production)
  avatar: String?

friendships/{friendshipId}
  id: Long
  senderId: Long
  receiverId: Long
  status: "PENDING" | "ACCEPTED"
  createdAt: Long

conversations/{conversationId}
  id: Long
  type: "PAIR" | "GROUP"
  memberIds: List<Long>
  conversationName: String?
  groupAvatar: String?
  pairAvatar: String?
  themeColor: String?
  lastContent: String?
  lastSenderId: Long?
  lastCreatedAt: Long?
  createdAt: Long?

messages/{messageId}
  id: Long
  conversationId: Long
  senderId: Long
  content: String?
  mediaFile: String?  (luôn null trên Firestore)
  createdAt: Long
  isRead: Boolean

feeds/{feedId}
  id: Long
  posterId: Long
  content: String?
  createdAt: Long
  mediaFile: String?  (luôn null trên Firestore)

fcm_tokens/{userId}
  userId: Long
  token: String
  updatedAt: Long
```

> Lưu ý: khi `use_firestore = true`, document `users/{uid}` dùng Firebase Auth UID làm key. Field `password` không còn được lưu vào Firestore vì xác thực do Firebase Authentication quản lý.

---

## 6. Hilt DI

- `MyApplication` được đánh dấu `@HiltAndroidApp` ⇒ sinh `ApplicationComponent`.
- `MainActivity` được đánh dấu `@AndroidEntryPoint`.
- Tất cả ViewModel được đánh dấu `@HiltViewModel` và inject các Repository qua constructor.
- `AppNavigation` lấy ViewModel bằng `hiltViewModel()` ⇒ scope theo `MainActivity` (chia sẻ giữa các destination).
- `AuthInterceptor` được Hilt inject `@ApplicationContext`, không cần truyền `Context` thủ công.

### Modules
| Module | Cung cấp |
| --- | --- |
| `NetworkModule` | `OkHttpClient`, `Retrofit`, `AuthApi`, `UserApi`, `FriendApi`, `ConversationApi`, `FeedApi`, `FcmApi` |
| `FirebaseModule` | `FirebaseFirestore`, `FirebaseRemoteConfig`, `FirebaseAuth` |
| `RepositoryModule` | Bind `*RepositoryImpl` ↔ `*Repository` (interface domain) |

---

## 7. Setup nhanh

1. Đảm bảo `app/google-services.json` đã có (đã commit sẵn).
2. Mở Firebase Console:
   - Bật **Cloud Firestore** (test/native mode) cho project `chat-application-dd146`.
   - Bật **Authentication** → **Sign-in method** → bật **Email/Password**.
   - Bật **Remote Config**, thêm `use_firestore` = `false` (default).
3. Đổi `BASE_URL` trong [`app/src/main/java/com/company/myapplication/repository/apiconfig/ApiConfig.kt`](app/src/main/java/com/company/myapplication/repository/apiconfig/ApiConfig.kt) cho phù hợp với backend của bạn.
4. Build & chạy:
   ```bash
   ./gradlew assembleDebug
   ```

---

## 8. Đề xuất cải tiến tiếp theo

- **Use case layer**: hiện Repository được inject thẳng vào ViewModel. Có thể thêm `domain/usecase/` (mỗi UseCase một function `invoke`) cho các business logic phức tạp.
- **Result wrapper**: thay `null`/`Boolean` bằng `sealed class Resource<T> { Loading, Success, Error }` để xử lý lỗi nhất quán trên UI.
- **DataStore thay SharedPreferences**: `UserSharedPreferences` là object, có thể chuyển sang DataStore + repository pattern để test dễ hơn.
- ~~**Firebase Authentication**: thay cơ chế username/password tự lưu trong `users` bằng Firebase Auth thực thụ.~~ *(Đã thực hiện — khi `use_firestore = true`, auth dùng `FirebaseAuth` email/password, ID Token thực.)*
- **Cloud Storage cho file**: muốn Firestore mode hỗ trợ media, dùng Firebase Storage + lưu URL vào Firestore (sẽ phải gỡ logic toast "không khả dụng").
- **Pagination** cho `messages` / `feeds` (Firestore: `.limit(...)` + cursor; REST: thêm `?page=&size=`).
- **Listener realtime**: dùng `addSnapshotListener` của Firestore để stream `messages` → Flow, thay vì `get()` một lần.
- **CI/CD**: thêm Github Actions chạy `./gradlew test lint`.

---

## 9. Build flags

- `kotlin = 2.3.21`, `agp = 8.13.2`, `hilt = 2.52`, `ksp` cho Hilt.
- `compileSdk = 36`, `minSdk = 26`, `targetSdk = 36`, `JVM 17`, core library desugaring bật.

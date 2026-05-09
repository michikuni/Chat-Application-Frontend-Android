<!-- Hiện tại đã có google-services.json để kết nối với firebase
Thực hiện yêu cầu sau:
1. Setup firebase remote config: Gồm 1 field là use_firestore kiểu bool, nếu set true thì dùng firestore, nếu false thì dùng backend.
2. Setup firestore như một option backend có thể switch với backend hiện tại một cách linh hoạt khi cần. Với phần gửi file hay những api mà firestore không thể lưu trữ thì sẽ hiện thông báo toast là "Chức năng hiện tại không khả dụng".
3. Refactor code lại theo clean architecture sử dụng hilt làm DI
4. Đề xuất plan chỉnh sửa (nếu có)
5. Viết document vào README.md -->

Thay đổi thêm như sau: Setup firebase auth để nếu use_firestore = true thì sử dụng hoàn toàn backend là firebase
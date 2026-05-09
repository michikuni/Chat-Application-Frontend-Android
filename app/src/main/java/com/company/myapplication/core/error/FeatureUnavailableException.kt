package com.company.myapplication.core.error

class FeatureUnavailableException(
    message: String = "Chức năng hiện tại không khả dụng"
) : RuntimeException(message)

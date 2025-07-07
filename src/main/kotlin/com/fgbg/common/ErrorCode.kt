package com.fgbg.com.fgbg.common

enum class ErrorCode(val code: Int, val msg: String) {
    SUCCESS(200, "成功"),
    SYSTEM_ERROR(500, "系统错误"),
    UNKNOWN_ERROR(500, "未知错误"),
    INVALID_INPUT(400, "输入无效");
}
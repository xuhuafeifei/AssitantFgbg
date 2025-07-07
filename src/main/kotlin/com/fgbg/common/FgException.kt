package com.fgbg.com.fgbg.common

class FgException(val code: Int, val msg: String) : RuntimeException("$code: $msg") {

    constructor(code: Int, msg: String, cause: Throwable) : this(code, msg) {
        initCause(cause)
    }

    constructor(errorCode: ErrorCode, msg: String) : this(errorCode.code, msg)
}
package com.fgbg.com.fgbg.common

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseBaseVo<T> (
    val code: Int = 0,
    val msg: String  = "",
    val data: T? = null,
) {
    companion object {
        fun <T> ok(data: T? = null) = ResponseBaseVo(200, "success", data)
        fun error(msg: String) = ResponseBaseVo(500, msg, null)
        fun error(code: Int, msg: String) = ResponseBaseVo(code, msg, null)
    }
}
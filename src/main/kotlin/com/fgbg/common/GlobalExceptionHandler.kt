package com.fgbg.common

import com.fgbg.com.fgbg.common.ErrorCode
import com.fgbg.com.fgbg.common.FgException
import com.fgbg.com.fgbg.common.ResponseBaseVo
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    companion object {
        private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseBaseVo<Nothing> {
        log.error("系统异常", e)
        return ResponseBaseVo.error(
            ErrorCode.SYSTEM_ERROR.code,
            e.message ?: "系统异常"
        )
    }

    @ExceptionHandler(FgException::class)
    fun handleFgException(e: FgException): ResponseBaseVo<Nothing> {
        log.error("系统异常", e)
        return ResponseBaseVo.error(
            e.code,
            e.message ?: "系统异常"
        )
    }
}
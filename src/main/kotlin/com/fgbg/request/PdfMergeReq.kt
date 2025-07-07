package com.fgbg.com.fgbg.request

import org.jetbrains.annotations.NotNull
import org.springframework.web.multipart.MultipartFile

data class PdfMergeReq(
    @field:NotNull(value = "preFile不能为空")
    val preFile: MultipartFile,

    @field:NotNull(value = "postFile不能为空")
    val postFile: MultipartFile,
)
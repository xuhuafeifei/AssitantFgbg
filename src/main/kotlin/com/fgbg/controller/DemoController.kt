package com.fgbg.com.fgbg.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoController {

    @GetMapping("/demo")
    fun demo(): String {
        return "Hello from Demo Controller"
    }
}
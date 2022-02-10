package com.example.fidosimpledemo.rpserver.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("index.html")
    public String handleIndex() {
        return "index";
    }
}

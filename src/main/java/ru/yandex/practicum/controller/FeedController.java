package ru.yandex.practicum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FeedController {

    @GetMapping("/feed")
    @ResponseBody
    public String feed() {
        return "<h1>Feed will be here</h1>";
    }

}

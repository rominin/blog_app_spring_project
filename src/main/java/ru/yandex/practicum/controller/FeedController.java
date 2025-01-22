package ru.yandex.practicum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.PostService;

import java.util.List;

@Controller
@RequestMapping("/feed")
public class FeedController {

    private final PostService postService;

    public FeedController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String feed(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "tag", required = false) String tag,
            Model model) {

        List<Post> posts;

        if (tag != null && !tag.isEmpty()) {
            posts = postService.getPostsByTagName(tag, page, size);
            model.addAttribute("tag", tag);
        } else {
            posts = postService.getPosts(page, size);
        }

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "feed";
    }

}

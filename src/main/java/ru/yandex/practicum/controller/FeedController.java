package ru.yandex.practicum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.service.TagService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/feed")
public class FeedController {

    private final PostService postService;
    private final TagService tagService;

    public FeedController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping
    public String feed(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "tag", required = false) String tag,
            Model model) {

        List<Post> posts;

        if (tag != null && !tag.isEmpty()) {
            Tag existingTag = tagService.findTagByName(tag);
            if (existingTag == null) {
                posts = new ArrayList<>();
                model.addAttribute("message", "No posts found for tag: " + tag);
            } else {
                posts = postService.getPostsByTagName(tag, page, size);
            }
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

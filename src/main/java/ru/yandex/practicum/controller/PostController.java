package ru.yandex.practicum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.service.TagService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final TagService tagService;

    public PostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("/new")
    public String showAddPostForm(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("tags", tagService.getAllTags());
        return "addPostForm";
    }

    @PostMapping("/new")
    public String addPost(@ModelAttribute Post post, @RequestParam (name = "tags") String tags) {
        List<String> tagNames = Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .collect(Collectors.toList());

        postService.addPost(post, tagNames);
        return "redirect:/feed";
    }

}

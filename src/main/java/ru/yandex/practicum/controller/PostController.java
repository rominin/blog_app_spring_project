package ru.yandex.practicum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.CommentService;
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
    private final CommentService commentService;

    public PostController(PostService postService, TagService tagService, CommentService commentService) {
        this.postService = postService;
        this.tagService = tagService;
        this.commentService = commentService;
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

    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable (name = "id") Long id, @RequestParam("text") String text) {
        commentService.addComment(id, text);
        return "redirect:/post/" + id;
    }


    @PostMapping("/{id}/comment/{commentId}/edit")
    public String editComment(@PathVariable (name = "id") Long id, @PathVariable (name = "commentId") Long commentId, @RequestParam("text") String text) {
        commentService.updateComment(commentId, text);
        return "redirect:/post/" + id;
    }

    @PostMapping("/{id}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable (name = "id") Long id, @PathVariable (name = "commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/post/" + id;
    }

}

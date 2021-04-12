package es.dawgroup2.juding.posts.rest;

import es.dawgroup2.juding.posts.Post;
import es.dawgroup2.juding.posts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostAPIController {
    @Autowired
    PostService postService;

    /**
     * Gets a simple post to view
     *
     * @param id post id
     * @return {@code True} response entity with the single post. {@code False} if bad request
     */
    @GetMapping("")
    public ResponseEntity<Post> newsPost(@RequestParam String id) {
        Post post = postService.findById(id);
        return (post == null) ? ResponseEntity.badRequest().build() : ResponseEntity.ok(post);
    }

    /**
     * Gets a recent post list
     *
     * @param id post id that is not in the list
     * @return {@code True} response entity with the post list. {@code False} if bad request
     */
    @GetMapping("/recent")
    public ResponseEntity<List<Post>> recentNews(@RequestParam String id) {
        List<Post> recentPosts = postService.findFirst5Desc(id);
        return ResponseEntity.ok(recentPosts);
    }

    /**
     * Gets a list with a post to view (pagination format)
     *
     * @param page page number
     * @return {@code True} response entity with the post to view list. {@code False} if bad request
     */
    @GetMapping("/page")
    public ResponseEntity<Page<Post>> getPage(@RequestParam(required = false) Integer page) {
        int defPage = (page == null) ? 1 : page - 1;
        if (defPage <= 0) return ResponseEntity.badRequest().build();
        Page<Post> requiredPage = postService.getPostsInPages(defPage, 3);
        return (requiredPage.hasContent()) ? ResponseEntity.ok(requiredPage) : ResponseEntity.badRequest().build();
    }
}

package com.yarou.book.feedback;

import com.yarou.book.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
@Tag(name="feedback")
public class FeedbackController {

    private final FeedbackService service;


    @PostMapping
    public ResponseEntity<Integer> saveFeedback(
            @Valid @RequestBody FeedbackRequest request,
            Authentication ConnectedUser
    )
    {
        return ResponseEntity.ok(service.save(request,ConnectedUser));
    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedBackResponse>> findAllFeedbacksByBook(
            @PathVariable("book-id") Integer bookId,
            @RequestParam(name="page", defaultValue = "0",required = false) int page,
            @RequestParam(name="size", defaultValue = "10",required = false) int size,
            Authentication ConnectedUser
    ){
        return ResponseEntity.ok(service.findAllFeedbacksByBook(bookId,page,size,ConnectedUser));
    }




}

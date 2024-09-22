package com.yarou.book.feedback;

import com.yarou.book.book.Book;

import java.util.Objects;

public class FeedbackMapper {


    public Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .book(Book.builder()
                        .id(request.bookId())
                        .archived(false)
                        .shareable(false)
                        .build())
                .build();

    }

    public FeedBackResponse toFeedbackResponse(Feedback feedback, Integer id) {
        return
                FeedBackResponse.builder()
                        .note(feedback.getNote())
                        .comment(feedback.getComment())
                        .ownFeedback(Objects.equals(feedback.getCreatedBy(),id))
                        .build();
    }
}

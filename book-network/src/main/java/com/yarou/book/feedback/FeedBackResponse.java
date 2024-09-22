package com.yarou.book.feedback;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedBackResponse {
    private Double note;
    private String comment;
    private Boolean ownFeedback;
}

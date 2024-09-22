package com.yarou.book.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackRespository extends JpaRepository<Feedback,Integer> {

    @Query("""
            SELECT feedback
            FROM Feedback feedback
            WHERE feedback.book.id = :bookId
            """)
    Page<Feedback> findAllBybookId(Integer bookId, Pageable pageable);
}

package com.yarou.book.book;

import com.yarou.book.common.BaseEntity;
import com.yarou.book.history.BookTransactionHistory;
import com.yarou.book.user.User;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {




    private String title;

    private  String authorName;

    private String isbn;

    private  String synopsis;

    private String bookCover;

    private boolean archived;

    private boolean shareable;


    @ManyToOne
    @JoinColumn(name="owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;

}

package com.yarou.book.history;

import com.yarou.book.book.Book;
import com.yarou.book.common.BaseEntity;
import com.yarou.book.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BookTransactionHistory extends BaseEntity {

    // user RelationShip
    @ManyToOne
    @JoinColumn(name="user_id")
     private User user;
    // book RelationShip
    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

    private boolean returned;
    private boolean returnApproved;

}

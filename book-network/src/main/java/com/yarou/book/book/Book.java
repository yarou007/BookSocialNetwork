package com.yarou.book.book;

import com.yarou.book.common.BaseEntity;
import com.yarou.book.feedback.Feedback;
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

    // String it's the file path of the uploded picture
    private String bookCover;

    private boolean archived;

    private boolean shareable;


    @ManyToOne
    @JoinColumn(name="owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    @Transient
    public double getRate(){
        if(feedbacks ==null ||feedbacks.isEmpty()){
            return 0.0;
        }

        // loopina aal feedback a travers stream w kdhina note w fonction average tehsb wahad'ha w traja3lek l average
        var  rate = this.feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);
         double roundedRate = Math.round(rate*10.0)/10.0;
        return roundedRate;
    }

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;

}

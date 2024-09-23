package com.yarou.book.common;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@Setter
@SuperBuilder // when u have inheritance normal build won't work u must use super builder to see super fields
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass /*
        Not a Table in the Database
        Is a class that provides common persistent fields or methods to other entity classes but is not itself an entity
        Reusable Base Class
        Fields declared in the @MappedSuperclass are inherited by subclasses and subclasses are mapped to database tables
        No Entity Relationships
*/
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue
    private Integer id;


    @CreatedDate
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;


    @Column(nullable = false,updatable = false)
    private Integer createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private Integer lastModifiedBy;
}

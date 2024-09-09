package com.yarou.book.book;

import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> withOwnerId(Integer ownerId){
        return (root,query,criteriaBuidler) -> criteriaBuidler.equal(root.get("owner").get("id"),ownerId);
    }
}

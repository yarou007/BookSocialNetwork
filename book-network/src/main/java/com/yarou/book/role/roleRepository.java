package com.yarou.book.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface roleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByName(String name);
}

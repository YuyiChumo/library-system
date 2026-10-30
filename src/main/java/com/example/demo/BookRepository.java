package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    // 判断这个书名是否已经存在（新增书籍时用来查重）
    boolean existsByName(String name);

    Book findByName(String name);
    Book getBookById(Integer id);
}
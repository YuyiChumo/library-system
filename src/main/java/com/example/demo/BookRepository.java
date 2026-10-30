package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    // 判断这个书名是否已经存在（新增书籍时用来查重）
    boolean existsByName(String name);

    // 查一条书：方法名 = get + Book + By + Id，Spring Data 会自动生成 where id = ?
    // （查全部直接用父类自带的 findAll()，不用在这里声明）
    Book getBookById(Integer id);
}
package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Integer> {
    // 查这个人借了这本书，且还没还（returnTime 为 null）的记录
    BorrowRecord findByUserAndBookAndReturnTimeIsNull(User user, Book book);

    // 查这个人所有的借阅记录（可选，以后做“我的借阅”页面用）
    List<BorrowRecord> findByUser(User user);
}
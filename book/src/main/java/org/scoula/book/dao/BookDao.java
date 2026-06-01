package org.scoula.book.dao;

import org.scoula.book.domain.BookImageVO;
import org.scoula.book.domain.BookVO;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    void insert(BookVO book);

    void insertImage(BookImageVO image);

    int getTotalCount();

    List<String> getCategories();

    List<BookVO> getBooks();

    List<BookVO> getBooks(int page);

    List<BookVO> getBooks(String category);

    Optional<BookVO> getBook(Long no);
}
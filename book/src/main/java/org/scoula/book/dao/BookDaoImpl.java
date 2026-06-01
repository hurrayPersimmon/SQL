package org.scoula.book.dao;

import org.scoula.book.domain.BookImageVO;
import org.scoula.book.domain.BookVO;
import org.scoula.database.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImpl implements BookDao {
    Connection conn = JDBCUtil.getConnection();

    @Override
    public void insert(BookVO book) {
        String sql = """
                insert into tbl_book
                (no, category, title, author, publisher, description, price)
                values (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, book.getNo());
            pstmt.setString(2, book.getCategory());
            pstmt.setString(3, book.getTitle());
            pstmt.setString(4, book.getAuthor());
            pstmt.setString(5, book.getPublisher());
            pstmt.setString(6, book.getDescription());
            pstmt.setInt(7, book.getPrice());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertImage(BookImageVO image) {
        String sql = "insert into tbl_book_image(filename, book_no) values(?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, image.getFilename());
            pstmt.setLong(2, image.getBookNo());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getTotalCount() {
        String sql = "select count(*) from tbl_book";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();

        String sql = "select distinct category from tbl_book order by category";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return categories;
    }

    private BookVO map(ResultSet rs) throws SQLException {
        return BookVO.builder()
            .no(rs.getLong("no"))
            .category(rs.getString("category"))
            .title(rs.getString("title"))
            .author(rs.getString("author"))
            .publisher(rs.getString("publisher"))
            .description(rs.getString("description"))
            .price(rs.getInt("price"))
            .build();
    }

    private BookImageVO mapImage(ResultSet rs) throws SQLException {
        return BookImageVO.builder()
            .no(rs.getLong("bino"))
            .filename(rs.getString("filename"))
            .bookNo(rs.getLong("book_no"))
            .build();
    }

    @Override
    public List<BookVO> getBooks() {
        List<BookVO> books = new ArrayList<>();

        String sql = "select * from tbl_book order by category, title";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                BookVO book = map(rs);
                books.add(book);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return books;
    }

    @Override
    public List<BookVO> getBooks(int page) {
        List<BookVO> books = new ArrayList<>();

        String sql = "select * from tbl_book order by category, title limit ?, ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int count = 10;
            int start = (page - 1) * count;

            pstmt.setInt(1, start);
            pstmt.setInt(2, count);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BookVO book = map(rs);
                    books.add(book);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return books;
    }

    @Override
    public List<BookVO> getBooks(String category) {
        List<BookVO> books = new ArrayList<>();

        String sql = "select * from tbl_book where category = ? order by title";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BookVO book = map(rs);
                    books.add(book);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return books;
    }

    @Override
    public Optional<BookVO> getBook(Long no) {
        BookVO book = null;

        String sql = """
                select b.*, bi.no as bino, bi.filename, bi.book_no
                from tbl_book b
                left outer join tbl_book_image bi
                on b.no = bi.book_no
                where b.no = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, no);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    book = map(rs);

                    List<BookImageVO> images = new ArrayList<>();

                    do {
                        String filename = rs.getString("filename");

                        if (filename != null) {
                            BookImageVO image = mapImage(rs);
                            images.add(image);
                        }

                    } while (rs.next());

                    book.setImages(images);

                    return Optional.of(book);
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
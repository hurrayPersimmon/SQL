package org.scoula.book.dataimport;

import com.opencsv.bean.CsvToBeanBuilder;
import org.scoula.book.dao.BookDao;
import org.scoula.book.dao.BookDaoImpl;
import org.scoula.book.domain.BookVO;
import org.scoula.database.JDBCUtil;

import java.io.FileReader;
import java.util.List;

public class ImportBookData {
    public static void main(String[] args) throws Exception {
        BookDao dao = new BookDaoImpl();

        List<BookVO> books = new CsvToBeanBuilder<BookVO>(new FileReader("book.csv"))
            .withType(BookVO.class)
            .build()
            .parse();

        books.forEach(book -> {
            System.out.println(book);
            dao.insert(book);
        });

        JDBCUtil.close();
    }
}
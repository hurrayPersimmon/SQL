package org.scoula;

import com.opencsv.bean.CsvToBeanBuilder;
import org.scoula.book.domain.BookVO;

import java.io.FileReader;
import java.util.List;

public class CSVTest2 {
    public static void main(String[] args) throws Exception {
        List<BookVO> books = new CsvToBeanBuilder<BookVO>(new FileReader("book.csv"))
            .withType(BookVO.class)
            .build()
            .parse();

        books.forEach(book -> {
            System.out.println(book);
        });
    }
}
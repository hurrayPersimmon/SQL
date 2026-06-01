package org.scoula.book.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookVO {
    private Long no;
    private String category;
    private String title;
    private String author;
    private String publisher;
    private String description;
    private Integer price;

    private List<BookImageVO> images;
}
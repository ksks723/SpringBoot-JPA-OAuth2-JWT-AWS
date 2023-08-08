package me.sku.springbootdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.sku.springbootdeveloper.domain.Article;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {
    private String title;
    private String content;

    public Article toEntity(String author){//빌더 패턴을 사용해 DTO를 엔티티로 만들어주는 메서드다. 블로그 글을 추가할 때 저장할 엔티티로 변환하는 용도이다.
        return Article.builder().title(title).content(content).author(author).build();
    }
}

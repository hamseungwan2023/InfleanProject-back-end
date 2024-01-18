package inflearnproject.anoncom.post.dto;

import lombok.Data;

import java.util.List;

@Data
public class PagingPost {

    private List<ResPostDto> dtos;
    private int currentPage;
    private int totalPage;

    public PagingPost(List<ResPostDto> dtos, int currentPage, int totalPage) {
        this.dtos = dtos;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
    }
}

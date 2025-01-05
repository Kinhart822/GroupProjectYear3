package vn.edu.usth.mcma.backend.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchMovieByNameResponse {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String backgroundImageUrl;
    private Integer length;//in seconds
    private String publishDate;
    private String trailerUrl;
    private RatingResponse rating;
    private List<GenreResponse> genres;
    private List<PerformerResponse> performers;
}


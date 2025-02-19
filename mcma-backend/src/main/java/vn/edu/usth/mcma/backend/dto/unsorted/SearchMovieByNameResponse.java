package vn.edu.usth.mcma.backend.dto.unsorted;

import lombok.*;
import vn.edu.usth.mcma.backend.dto.movie.GenrePresentation;
import vn.edu.usth.mcma.backend.dto.movie.PerformerPresentation;
import vn.edu.usth.mcma.backend.dto.movie.RatingPresentation;

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
    private String poster;
    private String banner;
    private Integer length;//in seconds
    private String publishDate;
    private String trailerUrl;
    private RatingPresentation rating;
    private List<GenrePresentation> genres;
    private List<PerformerPresentation> performers;
}


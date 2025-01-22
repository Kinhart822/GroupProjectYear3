package vn.edu.usth.mcma.frontend.dto.movie;

import java.util.Set;

import lombok.Data;

@Data
public class MovieDetailShort {
    private Long id;
    private String name;
    private Integer length;
    private String imageBase64;

    private Set<String> genres;
    private String rating;
}

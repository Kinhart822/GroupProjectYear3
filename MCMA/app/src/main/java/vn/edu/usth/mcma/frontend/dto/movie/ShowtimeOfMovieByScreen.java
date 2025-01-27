package vn.edu.usth.mcma.frontend.dto.movie;

import java.util.List;

import lombok.Data;

@Data
public class ShowtimeOfMovieByScreen {
    private Long screenId;
    private String screenType;
    private List<ShowtimeOfMovieBySchedule> showtimeOfMovieBySchedule;
}

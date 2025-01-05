package vn.edu.usth.mcma.frontend.ConnectAPI.Retrofit.APIs;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import vn.edu.usth.mcma.frontend.ConnectAPI.Model.Response.HighRatingMovieResponse;
import vn.edu.usth.mcma.frontend.ConnectAPI.Model.Response.ViewCinemaResponse;

public interface GetCinemaListAPI {
    @GET("/api/v1/user/view/cinemaList")
    Call<ViewCinemaResponse> getCinemaList();

    @GET("/api/v1/user/view/cinemaListByCity/{cityId}")
    Call<ViewCinemaResponse> getCinemaListByCity(@Path("cityId") int cityId);
}

package vn.edu.usth.mcma.frontend.Showtimes.Utils;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.usth.mcma.R;
import vn.edu.usth.mcma.frontend.ConnectAPI.Model.Response.ViewCityResponse;
import vn.edu.usth.mcma.frontend.ConnectAPI.Retrofit.APIs.GetAllCitiesAPI;
import vn.edu.usth.mcma.frontend.Showtimes.Models.Movie;
import vn.edu.usth.mcma.frontend.Showtimes.Models.Theater;

public class TheaterDataProvider {
    public static List<String> getCities() {
        return Arrays.asList("TPHCM", "Hà Nội", "Huế", "Đà Nẵng", "Cần Thơ", "Nha Trang", "Đà Lạt", "Vũng Tàu");
    }

    private static final String BASE_URL = "http://192.168.1.103:8080/";
    private static GetAllCitiesAPI apiService;

    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(GetAllCitiesAPI.class);
    }

    public static void getCities(Callback<ViewCityResponse> callback) {
        Call<ViewCityResponse> call = apiService.getAllCities();
        call.enqueue(callback);
    }

    private static final Map<String, List<TheaterInfo>> CITY_THEATERS = new HashMap<String, List<TheaterInfo>>() {{
        put("TPHCM", Arrays.asList(
                new TheaterInfo("CGV Aeon Mall Tân Phú", "30 Bờ Bao Tân Thắng, P. Sơn Kỳ, Q. Tân Phú", R.drawable.theater_image1),
                new TheaterInfo("BHD Star Bitexco", "L3-Bitexco Icon 68, 2 Hải Triều, Q.1", R.drawable.theater_image1),
                new TheaterInfo("Lotte Cinema Cantavil", "L7-Cantavil Premier, Xa lộ Hà Nội, Q.2", R.drawable.theater_image1),
                new TheaterInfo("CGV Vincom Thủ Đức", "216 Võ Văn Ngân, P. Bình Thọ, Q. Thủ Đức", R.drawable.theater_image1),
                new TheaterInfo("Galaxy Nguyễn Du", "116 Nguyễn Du, Q.1", R.drawable.theater_image1)
        ));
        put("Hà Nội", Arrays.asList(
                new TheaterInfo("CGV Vincom Royal City", "72A Nguyễn Trãi, Thanh Xuân", R.drawable.theater_image1),
                new TheaterInfo("BHD Star Discovery", "Discovery Complex, 302 Cầu Giấy", R.drawable.theater_image1),
                new TheaterInfo("Lotte Cinema Ba Đình", "54 Liễu Giai, Ba Đình", R.drawable.theater_image1),
                new TheaterInfo("Beta Cinemas Mỹ Đình", "Tòa nhà C3, Mỹ Đình 1, Nam Từ Liêm", R.drawable.theater_image1)
        ));
        put("Huế", Arrays.asList(
                new TheaterInfo("CGV Aeon Mall Tân Phú", "30 Bờ Bao Tân Thắng, P. Sơn Kỳ, Q. Tân Phú", R.drawable.theater_image1),
                new TheaterInfo("BHD Star Bitexco", "L3-Bitexco Icon 68, 2 Hải Triều, Q.1", R.drawable.theater_image1),
                new TheaterInfo("Lotte Cinema Cantavil", "L7-Cantavil Premier, Xa lộ Hà Nội, Q.2", R.drawable.theater_image1),
                new TheaterInfo("CGV Vincom Thủ Đức", "216 Võ Văn Ngân, P. Bình Thọ, Q. Thủ Đức", R.drawable.theater_image1),
                new TheaterInfo("Galaxy Nguyễn Du", "116 Nguyễn Du, Q.1", R.drawable.theater_image1)
        ));
        put("Đà Nẵng", Arrays.asList(
                new TheaterInfo("CGV Aeon Mall Tân Phú", "30 Bờ Bao Tân Thắng, P. Sơn Kỳ, Q. Tân Phú", R.drawable.theater_image1),
                new TheaterInfo("BHD Star Bitexco", "L3-Bitexco Icon 68, 2 Hải Triều, Q.1", R.drawable.theater_image1),
                new TheaterInfo("Lotte Cinema Cantavil", "L7-Cantavil Premier, Xa lộ Hà Nội, Q.2", R.drawable.theater_image1),
                new TheaterInfo("CGV Vincom Thủ Đức", "216 Võ Văn Ngân, P. Bình Thọ, Q. Thủ Đức", R.drawable.theater_image1),
                new TheaterInfo("Galaxy Nguyễn Du", "116 Nguyễn Du, Q.1", R.drawable.theater_image1)
        ));
        put("Cần Thơ", Arrays.asList(
                new TheaterInfo("CGV Aeon Mall Tân Phú", "30 Bờ Bao Tân Thắng, P. Sơn Kỳ, Q. Tân Phú", R.drawable.theater_image1),
                new TheaterInfo("BHD Star Bitexco", "L3-Bitexco Icon 68, 2 Hải Triều, Q.1", R.drawable.theater_image1),
                new TheaterInfo("Lotte Cinema Cantavil", "L7-Cantavil Premier, Xa lộ Hà Nội, Q.2", R.drawable.theater_image1),
                new TheaterInfo("CGV Vincom Thủ Đức", "216 Võ Văn Ngân, P. Bình Thọ, Q. Thủ Đức", R.drawable.theater_image1),
                new TheaterInfo("Galaxy Nguyễn Du", "116 Nguyễn Du, Q.1", R.drawable.theater_image1)
        ));
        put("Nha Trang", Arrays.asList(
                new TheaterInfo("CGV Aeon Mall Tân Phú", "30 Bờ Bao Tân Thắng, P. Sơn Kỳ, Q. Tân Phú", R.drawable.theater_image1),
                new TheaterInfo("BHD Star Bitexco", "L3-Bitexco Icon 68, 2 Hải Triều, Q.1", R.drawable.theater_image1),
                new TheaterInfo("Lotte Cinema Cantavil", "L7-Cantavil Premier, Xa lộ Hà Nội, Q.2", R.drawable.theater_image1),
                new TheaterInfo("CGV Vincom Thủ Đức", "216 Võ Văn Ngân, P. Bình Thọ, Q. Thủ Đức", R.drawable.theater_image1),
                new TheaterInfo("Galaxy Nguyễn Du", "116 Nguyễn Du, Q.1", R.drawable.theater_image1)
        ));
        put("Đà Lạt", Arrays.asList(
                new TheaterInfo("CGV Aeon Mall Tân Phú", "30 Bờ Bao Tân Thắng, P. Sơn Kỳ, Q. Tân Phú", R.drawable.theater_image1),
                new TheaterInfo("BHD Star Bitexco", "L3-Bitexco Icon 68, 2 Hải Triều, Q.1", R.drawable.theater_image1),
                new TheaterInfo("Lotte Cinema Cantavil", "L7-Cantavil Premier, Xa lộ Hà Nội, Q.2", R.drawable.theater_image1),
                new TheaterInfo("CGV Vincom Thủ Đức", "216 Võ Văn Ngân, P. Bình Thọ, Q. Thủ Đức", R.drawable.theater_image1),
                new TheaterInfo("Galaxy Nguyễn Du", "116 Nguyễn Du, Q.1", R.drawable.theater_image1)
        ));
        put("Vũng Tàu", Arrays.asList(
                new TheaterInfo("CGV Aeon Mall Tân Phú", "30 Bờ Bao Tân Thắng, P. Sơn Kỳ, Q. Tân Phú", R.drawable.theater_image1),
                new TheaterInfo("BHD Star Bitexco", "L3-Bitexco Icon 68, 2 Hải Triều, Q.1", R.drawable.theater_image1),
                new TheaterInfo("Lotte Cinema Cantavil", "L7-Cantavil Premier, Xa lộ Hà Nội, Q.2", R.drawable.theater_image1),
                new TheaterInfo("CGV Vincom Thủ Đức", "216 Võ Văn Ngân, P. Bình Thọ, Q. Thủ Đức", R.drawable.theater_image1),
                new TheaterInfo("Galaxy Nguyễn Du", "116 Nguyễn Du, Q.1", R.drawable.theater_image1)
        ));
    }};

    private static final ArrayList<String> MOVIES = new ArrayList<>(Arrays.asList(
            "The Dark Knight",
            "Inception",
            "Interstellar",
            "The Matrix",
            "Avengers: Endgame",
            "Spider-Man: No Way Home",
            "Black Panther",
            "Joker",
            "Parasite",
            "Dune"
    ));

    public static List<Theater> getTheatersForCity(String city) {
        List<TheaterInfo> allTheaters = CITY_THEATERS.getOrDefault(city, new ArrayList<>());
        List<Theater> theaters = new ArrayList<>();

        for (TheaterInfo info : allTheaters) {
            theaters.add(new Theater(
                    info.id,
                    info.name,
                    info.address,
                    city,
                    info.imageResId
            ));
        }

        return theaters;
    }

    public static List<Movie> getMoviesForTheater(String date, Activity activity) {
        List<Movie> movies = new ArrayList<>();

        String movieTitle = activity.getIntent().getStringExtra("MOVIE_TITLE");

        if (movieTitle != null && !movieTitle.isEmpty()) {
            boolean movieExists = false;
            for (String movie : MOVIES) {
                if (movie.equalsIgnoreCase(movieTitle)) {
                    movieExists = true;
                    break;
                }
            }

            if (!movieExists) {
                MOVIES.add(movieTitle);
            }
        }

        for (String movie : MOVIES) {
            movies.add(new Movie(
                    "movie_" + movie.toLowerCase().replace(" ", "_"),
                    movie,
                    generateShowtimes()
            ));
        }

        return movies;
    }

    public static List<String> generateShowtimes() {
        List<String> showtimes = new ArrayList<>();
        int startHour = 10; // 10 AM
        int endHour = 22;   // 10 PM

        for (int hour = startHour; hour <= endHour; hour += 2) {
            showtimes.add(String.format("%02d:00", hour));
        }

        return showtimes;
    }

    private static class TheaterInfo {
        int id;
        String idCity;
        String name;
        String address;
        int imageResId;

        public TheaterInfo(String address, int id, int imageResId, String name) {
            this.address = address;
            this.id = id;
            this.imageResId = imageResId;
            this.name = name;
        }

        public TheaterInfo(String address, String idCity, int imageResId) {
            this.address = address;
            this.idCity = idCity;
            this.imageResId = imageResId;
        }
    }
}
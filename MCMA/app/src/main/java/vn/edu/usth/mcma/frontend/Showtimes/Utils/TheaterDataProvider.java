package vn.edu.usth.mcma.frontend.Showtimes.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import vn.edu.usth.mcma.R;
import vn.edu.usth.mcma.frontend.Showtimes.Models.Movie;
import vn.edu.usth.mcma.frontend.Showtimes.Models.Theater;

public class TheaterDataProvider {
    // List of sample theater images (add these to your drawable folder)
    public static List<String> getCities() {
        return Arrays.asList("TPHCM", "Hà Nội", "Huế", "Đà Nẵng", "Cần Thơ", "Nha Trang", "Đà Lạt", "Vũng Tàu");
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

    private static final String[] MOVIES = {
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
    };

    public static List<Theater> getTheatersForCity(String city) {
        List<TheaterInfo> allTheaters = CITY_THEATERS.getOrDefault(city, new ArrayList<>());
        List<Theater> theaters = new ArrayList<>();

        for (TheaterInfo info : allTheaters){
            theaters.add(new Theater(
                    "theater_" + city.toLowerCase() + "_" + info.name.hashCode(),
                    info.name,
                    info.address,
                    city,
                    info.imageResId
            ));
        }

        return theaters;
    }

    public static List<Movie> getMoviesForTheater(String date) {
        List<Movie> movies = new ArrayList<>();
        for (String movieTitle : MOVIES){
            movies.add(new Movie(
                    "movie_" + movieTitle.toLowerCase().replace(" ", "_"),
                    movieTitle,
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
        String name;
        String address;
        int imageResId;

        TheaterInfo(String name, String address, int imageResId) {
            this.name = name;
            this.address = address;
            this.imageResId = imageResId;
        }
    }
}
package vn.edu.usth.mcma.frontend.Showtimes.UI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.mcma.R;
import vn.edu.usth.mcma.frontend.ConnectAPI.Model.Response.BookingProcess.Genre;
import vn.edu.usth.mcma.frontend.ConnectAPI.Model.Response.MovieResponse;
import vn.edu.usth.mcma.frontend.ConnectAPI.Model.Response.Performer;
import vn.edu.usth.mcma.frontend.ConnectAPI.Model.Response.Review;
import vn.edu.usth.mcma.frontend.ConnectAPI.Model.Response.ScheduleSelectedByCinemaResponse;
import vn.edu.usth.mcma.frontend.ConnectAPI.Retrofit.APIs.GetAllInformationOfSelectedMovie;
import vn.edu.usth.mcma.frontend.ConnectAPI.Retrofit.APIs.GetAllMovieAPI;
import vn.edu.usth.mcma.frontend.ConnectAPI.Retrofit.APIs.GetScheduleAPI;
import vn.edu.usth.mcma.frontend.ConnectAPI.Retrofit.RetrofitService;
import vn.edu.usth.mcma.frontend.Showtimes.Models.Movie;
import vn.edu.usth.mcma.frontend.Showtimes.Adapters.MovieScheduleAdapter;

public class TheaterScheduleActivity extends AppCompatActivity
        implements MovieScheduleAdapter.OnShowtimeClickListener {
    private MovieScheduleAdapter movieAdapter;
    private String selectedDate;
    private Button selectedDateButton;
    private int theaterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater_schedule);

        // Get theater details from intent
        theaterId = getIntent().getIntExtra("CINEMA_ID", -1);
        String theaterName = getIntent().getStringExtra("THEATER_NAME");
        String theaterAddress = getIntent().getStringExtra("THEATER_ADDRESS");

        // Set theater details in toolbar
        TextView nameTextView = findViewById(R.id.theater_name);
        TextView addressTextView = findViewById(R.id.theater_address);
        nameTextView.setText(theaterName);
        addressTextView.setText(theaterAddress);

        // Setup back button
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        setupViews();
        setupDateButtons(theaterId);
    }

    private void setupViews() {
        RecyclerView movieRecyclerView = findViewById(R.id.movie_recycler_view);
        movieAdapter = new MovieScheduleAdapter(this);
        movieRecyclerView.setAdapter(movieAdapter);
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupDateButtons(int theaterId) {
        LinearLayout daysContainer = findViewById(R.id.days_container);
        daysContainer.removeAllViews();
        RetrofitService retrofitService = new RetrofitService(this);
        GetScheduleAPI apiService = retrofitService
                .getRetrofit()
                .create(GetScheduleAPI.class);

        Call<ScheduleSelectedByCinemaResponse> call = apiService.getScheduleByCinema(theaterId);
        call.enqueue(new Callback<ScheduleSelectedByCinemaResponse>() {
            @Override
            public void onResponse(@NonNull Call<ScheduleSelectedByCinemaResponse> call, @NonNull Response<ScheduleSelectedByCinemaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ScheduleSelectedByCinemaResponse scheduleResponse = response.body();

                    List<String> uniqueDates = new ArrayList<>();
                    List<String> uniqueDateEntries = new ArrayList<>();

                    if (scheduleResponse.getDate() != null && scheduleResponse.getDayOfWeek() != null
                            && scheduleResponse.getDate().size() == scheduleResponse.getDayOfWeek().size()) {

                        for (int i = 0; i < scheduleResponse.getDate().size(); i++) {
                            String date = scheduleResponse.getDate().get(i);
                            String dayOfWeek = scheduleResponse.getDayOfWeek().get(i);
                            String uniqueKey = dayOfWeek + "\n" + date;

                            if (!uniqueDateEntries.contains(uniqueKey)) {
                                uniqueDateEntries.add(uniqueKey);
                                uniqueDates.add(date);
                            }
                        }
                    }

                    if (uniqueDates.isEmpty()) {
                        Toast.makeText(TheaterScheduleActivity.this, "No schedule found for this cinema.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (int i = 0; i < uniqueDates.size(); i++) {
                        final String originalDate = uniqueDates.get(i); // Dạng yyyy-MM-dd
                        final String formattedDate = convertDateFormat(originalDate); // Chuyển thành dd/MM/yyyy
                        final String buttonText = uniqueDateEntries.get(i);

                        Button dayButton = new Button(TheaterScheduleActivity.this);
                        dayButton.setText(buttonText);
                        dayButton.setAllCaps(false);
                        dayButton.setGravity(Gravity.CENTER);

                        // Thiết lập giao diện cho button
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(8, 0, 8, 0);
                        int paddingInDp = 10;
                        int paddingInPx = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                paddingInDp,
                                getResources().getDisplayMetrics()
                        );
                        dayButton.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);
                        dayButton.setLayoutParams(params);
                        dayButton.setBackground(getDrawable(R.drawable.date_button_selector));
                        dayButton.setTextColor(Color.BLACK);

                        // Đặt ngày đầu tiên được chọn mặc định
                        if (i == 0) {
                            dayButton.setSelected(true);
                            selectedDateButton = dayButton;
                            selectedDate = formattedDate;
                            loadMovieSchedule(formattedDate); // Tải lịch chiếu cho ngày đầu tiên
                        }

                        // Xử lý khi người dùng chọn ngày
                        dayButton.setOnClickListener(v -> {
                            if (selectedDateButton != null) {
                                selectedDateButton.setSelected(false);
                            }
                            dayButton.setSelected(true);
                            selectedDateButton = dayButton;
                            selectedDate = formattedDate;
                            loadMovieSchedule(formattedDate); // Tải lịch chiếu theo ngày đã chọn
                        });

                        daysContainer.addView(dayButton);
                    }
                } else {
                    Toast.makeText(TheaterScheduleActivity.this, "No schedule found for this cinema.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ScheduleSelectedByCinemaResponse> call, @NonNull Throwable t) {
                Log.e("TheaterScheduleError", "Error fetching schedule", t);
                Toast.makeText(TheaterScheduleActivity.this, "Failed to load schedule. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMovieSchedule(String date) {
        RetrofitService retrofitService = new RetrofitService(this);
        GetAllMovieAPI apiService = retrofitService
                .getRetrofit()
                .create(GetAllMovieAPI.class);

        Call<List<MovieResponse>> call = apiService.getAllMovieBySelectedDate(date);
        call.enqueue(new Callback<List<MovieResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<MovieResponse>> call, @NonNull Response<List<MovieResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<Integer, Long> movieIdMap = new HashMap<>();

                    // Lọc lịch chiếu theo ngày đã chọn
                    List<Movie> movies = response
                            .body()
                            .stream()
                            .map(m -> Movie
                                    .builder()
                                    .movieId(m.getId())
                                    .movieName(m.getName())
                                    .title(m.getName())
                                    .movieLength(m.getLength())
                                    .description(m.getDescription())
                                    .publishDate(m.getPublishDate())
                                    .trailerUrl(m.getTrailerUrl())
                                    .imageUrl(m.getImageUrl())
                                    .backgroundImageUrl(m.getBackgroundImageUrl())
                                    .schedules(m.getSchedules())
                                    .genres(m.getGenres())
                                    .performers(m.getPerformers())
                                    .rating(m.getRating())
                                    .reviews(m.getReviews())
                                    .build())
                            .collect(Collectors.toList());
                    AtomicInteger i = new AtomicInteger(0);
                    movies.forEach(m -> movieIdMap.put(i.getAndIncrement(), m.getMovieId()));

                    movieAdapter.setMovies(movies); // Cập nhật adapter với danh sách phim
                    movieAdapter.setOnShowtimeClickListener((movie, showtime) -> {
                        int position = movies.indexOf(movie);
                        if (position != -1 && movieIdMap.containsKey(position)) {
                            Long movieId = movieIdMap.get(position);
                            assert movieId != null;
                            fetchMovieDetails(movieId); // Fetch additional details
                        }
                    });


                } else {
                    Toast.makeText(TheaterScheduleActivity.this, "No schedule found for this date.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MovieResponse>> call, @NonNull Throwable t) {
                Log.e("TheaterScheduleError", "Error fetching schedule", t);
                Toast.makeText(TheaterScheduleActivity.this, "Failed to load schedule. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMovieDetails(Long movieId) {
        RetrofitService retrofitService = new RetrofitService(this);
        GetAllInformationOfSelectedMovie apiService = retrofitService
                .getRetrofit()
                .create(GetAllInformationOfSelectedMovie.class);

        Call<MovieResponse> call = apiService.getAllInformationOfSelectedMovie(movieId);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieResponse movieResponse = response.body();
                    // Handle movie details (e.g., display in a new activity)
                    Intent intent = new Intent(TheaterScheduleActivity.this, MovieDetailsActivity.class);
                    intent.putExtra("MOVIE_TITLE", movieResponse.getName());
                    intent.putExtra("MOVIE_NAME", movieResponse.getName());
                    intent.putExtra("MOVIE_GENRES", new ArrayList<>(movieResponse.getGenres().stream().map(Genre::getName).collect(Collectors.toList())));
                    intent.putExtra("MOVIE_LENGTH", movieResponse.getLength());
                    intent.putExtra("MOVIE_DESCRIPTION", movieResponse.getDescription());
                    intent.putExtra("PUBLISHED_DATE", movieResponse.getPublishDate());
                    intent.putExtra("IMAGE_URL", movieResponse.getImageUrl());
                    intent.putExtra("BACKGROUND_IMAGE_URL", movieResponse.getBackgroundImageUrl());
                    intent.putExtra("TRAILER", movieResponse.getTrailerUrl());
                    intent.putExtra("MOVIE_RATING", movieResponse.getRating().getName());
                    intent.putExtra("MOVIE_PERFORMER_NAME", new ArrayList<>(movieResponse.getPerformers().stream().map(Performer::getName).collect(Collectors.toList())));
                    intent.putStringArrayListExtra("MOVIE_PERFORMER_TYPE", new ArrayList<>(movieResponse.getPerformers().stream().map(Performer::getType).collect(Collectors.toList())));
                    intent.putExtra("MOVIE_COMMENT", new ArrayList<>(movieResponse.getReviews().stream().map(Review::getUserComment).collect(Collectors.toList())));
                    intent.putExtra("AVERAGE_STAR", movieResponse.getReviews().stream().mapToInt(Review::getUserVote).average().orElse(0.0));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Log.e("TheaterScheduleError", "Error fetching movie details", t);
                Toast.makeText(TheaterScheduleActivity.this, "Failed to load movie details. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onShowtimeClick(Movie movie, String showtime) {
        // Handle showtime selection
        Toast.makeText(this,
                "Selected: " + movie.getMovieName() + " at " + showtime,
                Toast.LENGTH_SHORT).show();
    }

    private String convertDateFormat(String originalDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = inputFormat.parse(originalDate);
            assert date != null;
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return originalDate; // Nếu có lỗi, trả về ngày gốc không thay đổi
        }
    }
}
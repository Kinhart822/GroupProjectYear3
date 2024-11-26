package vn.edu.usth.mcma.frontend.Feedback;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import vn.edu.usth.mcma.R;

public class RatingMovie_Activity extends AppCompatActivity {
    private EditText editFeedback;
    private Button buttonSubmit;
    private RatingBar ratingBar;
    private TextView ratingScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_movie);

        // Ánh xạ các View
        TextView movieName = findViewById(R.id.tvMovieName); // Tên phim
        TextView movieType = findViewById(R.id.tvMovieType); // Thể loại phim
        ImageButton mImageView = findViewById(R.id.back_button);
        ratingBar = findViewById(R.id.ratingBar);
        ratingScale = findViewById(R.id.tvRatingScale);
        editFeedback = findViewById(R.id.etComment);
        buttonSubmit = findViewById(R.id.btnSubmit);

        // Lấy dữ liệu từ intent trong adapter
        String name = getIntent().getStringExtra("movie_name");
        String type = getIntent().getStringExtra("movie_type");

        // Chỗ này sẽ thay tên phim và thể loại vô chỗ được set id trong xml
        movieName.setText(name);
        movieType.setText(type);

        mImageView.setOnClickListener(v -> finish());

        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            String ratingText;
            if (rating <= 1) {
                ratingText = "Terrible";
            } else if (rating <= 2) {
                ratingText = "Bad";
            } else if (rating <= 3) {
                ratingText = "Okay";
            } else if (rating <= 4) {
                ratingText = "Good";
            } else {
                ratingText = "Excellent!";
            }
            ratingScale.setText(ratingText);
        });

        buttonSubmit.setOnClickListener(v -> {
            String feedback = editFeedback.getText().toString().trim();
            float rating = ratingBar.getRating();

            if (feedback.isEmpty()) {
                Toast.makeText(this, "Please leave a comment", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Thank you for rating: " + rating + " stars\nFeedback: " + feedback, Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("rating", rating);
                resultIntent.putExtra("feedback", feedback);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}

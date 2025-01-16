package vn.edu.usth.mcma.frontend.Personal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.mcma.R;
import vn.edu.usth.mcma.frontend.ConnectAPI.Model.Response.BookingResponse;
import vn.edu.usth.mcma.frontend.ConnectAPI.Retrofit.APIs.GetAllBookingAPI;
import vn.edu.usth.mcma.frontend.ConnectAPI.Retrofit.RetrofitService;

public class Booking_History_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookingHistory_Adapter adapter;
    //    private List<BookingHistory_Item> items;
    private FrameLayout noDataContainer;
    private List<BookingResponse> items;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        recyclerView = findViewById(R.id.recyclerviewbooking_history);
        noDataContainer = findViewById(R.id.booking_history_no_data_container);

        items = new ArrayList<>();
        adapter = new BookingHistory_Adapter(this, items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ImageButton backButton = findViewById(R.id.booking_history_back_button);
        backButton.setOnClickListener(view -> {
            onBackPressed();
        });

        showNoDataView();
        fetchBookingHistory();

//        items.add(new BookingHistory_Item("5","Spider Man - Across The Spider Verse part 2", "07/11", "08/11", "Pending"));
//        items.add(new BookingHistory_Item("5","Spider Man - Across The Spider Verse part 2", "07/11", "08/11", "Pending"));
//        items.add(new BookingHistory_Item("5","Spider Man - Across The Spider Verse part 2", "07/11", "08/11", "Pending"));
//        items.add(new BookingHistory_Item("5","Spider Man - Across The Spider Verse part 2", "07/11", "08/11", "Pending"));
//        items.add(new BookingHistory_Item("5","Spider Man - Across The Spider Verse part 2", "07/11", "08/11", "Pending"));
    }

    void showNoDataView() {
        recyclerView.setVisibility(View.GONE);
        noDataContainer.setVisibility(View.VISIBLE);
    }

    void hideNoDataView() {
        recyclerView.setVisibility(View.VISIBLE);
        noDataContainer.setVisibility(View.GONE);
    }

    private void fetchBookingHistory() {
        RetrofitService retrofitService = new RetrofitService(this);
        GetAllBookingAPI getAllBookingAPI = retrofitService.getRetrofit().create(GetAllBookingAPI.class);
        getAllBookingAPI.getBookingHistory().enqueue(new Callback<List<BookingResponse>>() {
            @Override
            public void onResponse(Call<List<BookingResponse>> call, Response<List<BookingResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    items.clear();

                    if (response.body().isEmpty()) {
                        showNoDataView();
                    } else {
                        items.addAll(response.body());
                        hideNoDataView();
                        adapter.notifyDataSetChanged();
                    }
                } else {
//                    Toast.makeText(Booking_History_Activity.this, "Failed to load bookings", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BookingResponse>> call, Throwable t) {
                showNoDataView();
//                Toast.makeText(Booking_History_Activity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
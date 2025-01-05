package vn.edu.usth.mcma.frontend.Personal;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.mcma.R;
import vn.edu.usth.mcma.frontend.ConnectAPI.Model.Response.BookingResponse;
import vn.edu.usth.mcma.frontend.ConnectAPI.Retrofit.APIs.BookingProcessAPIs.BookingAPI;
import vn.edu.usth.mcma.frontend.ConnectAPI.Retrofit.RetrofitService;

public class Cancel_Booking_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Cancel_Booking_Adapter adapter;
    private List<Cancel_Booking_Item> items;
    private BookingAPI bookingAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_booking);

        recyclerView = findViewById(R.id.recyclerview_cancel_booking);

        items = new ArrayList<>();
//        items.add(new Cancel_Booking_Item("Wolverine", "Action", R.drawable.movie4));
//        items.add(new Cancel_Booking_Item("IronMan", "Drama", R.drawable.movie13));
//        items.add(new Cancel_Booking_Item("Wicked", "Comedy", R.drawable.movie12));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Cancel_Booking_Adapter(this, items);
        recyclerView.setAdapter(adapter);

        RetrofitService retrofitService = new RetrofitService(this);
        bookingAPI = retrofitService.getRetrofit().create(BookingAPI.class);

        fetchCompletedBookings();

        ImageButton backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void fetchCompletedBookings() {
        bookingAPI.getAllCompletedBookingsByUser().enqueue(new Callback<List<BookingResponse>>() {
            @Override
            public void onResponse(Call<List<BookingResponse>> call, Response<List<BookingResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Clear current items
                    items.clear();
                    // Map BookingResponse to Cancel_Booking_Item
                    for (BookingResponse booking : response.body()) {
                        items.add(new Cancel_Booking_Item(
                                booking.getBookingId(),
                                booking.getMovieName(),
                                "Booking No: " + booking.getBookingNo(),
                                booking.getImageUrlMovie(),
                                booking.getStartDateTime()
                        ));
                    }
                    // Notify adapter about data changes
                    adapter.notifyDataSetChanged();
                } else {
//                    Toast.makeText(Cancel_Booking_Activity.this, "Failed to fetch bookings", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BookingResponse>> call, Throwable t) {
//                Toast.makeText(Cancel_Booking_Activity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

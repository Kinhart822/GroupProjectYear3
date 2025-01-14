package vn.edu.usth.mcma.frontend.component.Showtimes.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.mcma.R;
import vn.edu.usth.mcma.frontend.dto.Request.BookingRequest;
import vn.edu.usth.mcma.frontend.dto.response.BookingProcess.SendBookingResponse;
import vn.edu.usth.mcma.frontend.dto.response.BookingResponse;
import vn.edu.usth.mcma.frontend.component.Login.Register_Activity;
import vn.edu.usth.mcma.frontend.component.Showtimes.Adapters.PaymentMethodAdapter;
import vn.edu.usth.mcma.frontend.component.Showtimes.Models.Booking.PaymentMethod;
import vn.edu.usth.mcma.frontend.constant.IntentKey;
import vn.edu.usth.mcma.frontend.network.ApiService;

public class PayingMethodActivity extends AppCompatActivity {
    private RecyclerView paymentMethodsRecyclerView;
    private CheckBox termsCheckbox;
    private Button completePaymentButton;
    private PaymentMethod selectedPaymentMethodEnum;
    private vn.edu.usth.mcma.frontend.component.Showtimes.Models.PaymentMethod selectedPaymentMethod;
    private int movieId;
    private int selectedCityId;
    private int selectedCinemaId;
    private int selectedScreenId;
    private int selectedScheduleId;
    private List<Integer> selectedTicketIds = new ArrayList<>();
    private final List<Integer> selectedSeatIds = new ArrayList<>();
    private List<Integer> selectedFoodIds = new ArrayList<>();
    private List<Integer> selectedDrinkIds = new ArrayList<>();
    private int selectedMovieCouponId;
    private int selectedUserCouponId;
    private static String bookingMessageSuccessFully;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paying_method);

        selectedScheduleId = getIntent().getIntExtra(IntentKey.SELECTED_SCHEDULE_ID.name(), -1);
        selectedTicketIds = getIntent().getIntegerArrayListExtra(IntentKey.SELECTED_TICKET_IDS.name());
//        selectedRootSeats = getIntent().getParcelableArrayListExtra(IntentKey.SELECTED_ROOT_SEATS.name());
        selectedFoodIds = getIntent().getIntegerArrayListExtra(IntentKey.SELECTED_FOOD_IDS.name());
        selectedDrinkIds = getIntent().getIntegerArrayListExtra(IntentKey.SELECTED_DRINK_IDS.name());
        selectedMovieCouponId = getIntent().getIntExtra(IntentKey.SELECTED_MOVIE_COUPON_ID.name(), -1);
        selectedUserCouponId = getIntent().getIntExtra(IntentKey.SELECTED_USER_COUPON_ID.name(), -1);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> onBackPressed());

        initializeViews();
        setupPaymentMethodsRecyclerView();
        handlePaymentCompletion();
    }

    private void initializeViews() {
        TextView movieTitleTV = findViewById(R.id.movieTitle);
        TextView theaterNameTV = findViewById(R.id.theaterName);
        TextView screenNumberTV = findViewById(R.id.screen_number);
        TextView movieDateTV = findViewById(R.id.movieDate);
        TextView movieShowtimeTV = findViewById(R.id.movieDuration);
        termsCheckbox = findViewById(R.id.termsCheckbox);
        completePaymentButton = findViewById(R.id.completePaymentButton);
        paymentMethodsRecyclerView = findViewById(R.id.paymentMethodsRecyclerView);

        String selectedMovie = getIntent().getStringExtra(IntentKey.MOVIE_NAME.name());
        if (selectedMovie != null) {
            movieTitleTV.setText(selectedMovie);
        }

        String selectedTheater = getIntent().getStringExtra(IntentKey.CINEMA_NAME.name());
        if (selectedTheater != null) {
            theaterNameTV.setText(selectedTheater);
        }

        String selectedDate = getIntent().getStringExtra(IntentKey.SELECTED_DATE.name());
        if (movieDateTV != null) {
            movieDateTV.setText(selectedDate);
        }

        // Screen number handling
        String selectedScreenRoom = getIntent().getStringExtra(IntentKey.SELECTED_SCREEN_ROOM.name());
        if (screenNumberTV != null) {
            screenNumberTV.setText(selectedScreenRoom != null ? selectedScreenRoom : "Screen 1");
        }

        String selectedShowtime = getIntent().getStringExtra(IntentKey.SELECTED_SHOWTIME.name());
        if (movieShowtimeTV != null) {
            movieShowtimeTV.setText(selectedShowtime);
        }
    }

//    private void setupPaymentMethodsRecyclerView() {
//        List<vn.edu.usth.mcma.frontend.Showtimes.Models.Booking.PaymentMethod> paymentMethods = Arrays.asList(vn.edu.usth.mcma.frontend.Showtimes.Models.Booking.PaymentMethod.Cash, vn.edu.usth.mcma.frontend.Showtimes.Models.Booking.PaymentMethod.Bank_Transfer);
//
//        paymentMethodAdapter = new PaymentMethodAdapter(paymentMethods);
//        paymentMethodAdapter.setOnPaymentMethodSelectedListener(paymentMethod -> {
//            selectedPaymentMethod = paymentMethod;
//            Toast.makeText(this, "Selected: " + paymentMethod.name(), Toast.LENGTH_SHORT).show();
//        });
//
//        paymentMethodsRecyclerView.setHasFixedSize(true);
//        paymentMethodsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        paymentMethodsRecyclerView.setAdapter(paymentMethodAdapter);
//    }

    private void setupPaymentMethodsRecyclerView() {
        List<vn.edu.usth.mcma.frontend.component.Showtimes.Models.PaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.add(new vn.edu.usth.mcma.frontend.component.Showtimes.Models.PaymentMethod("Cổng VNPAY", R.drawable.ic_vnpay));
        paymentMethods.add(new vn.edu.usth.mcma.frontend.component.Showtimes.Models.PaymentMethod("Ví Momo", R.drawable.ic_momo));
        paymentMethods.add(new vn.edu.usth.mcma.frontend.component.Showtimes.Models.PaymentMethod("Ví Zalopay", R.drawable.ic_zalopay));
        paymentMethods.add(new vn.edu.usth.mcma.frontend.component.Showtimes.Models.PaymentMethod("Thẻ ATM nội địa (Internet Banking)", R.drawable.ic_atm));
        paymentMethods.add(new vn.edu.usth.mcma.frontend.component.Showtimes.Models.PaymentMethod("Thẻ quốc tế (Visa, Master, Amex, JCB)", R.drawable.ic_credit_card));
        paymentMethods.add(new vn.edu.usth.mcma.frontend.component.Showtimes.Models.PaymentMethod("Ví ShopeePay", R.drawable.ic_shopeepay));

        Set<String> uniqueNames = new LinkedHashSet<>();
        List<vn.edu.usth.mcma.frontend.component.Showtimes.Models.PaymentMethod> uniquePaymentMethods = new ArrayList<>();
        for (vn.edu.usth.mcma.frontend.component.Showtimes.Models.PaymentMethod method : paymentMethods) {
            if (uniqueNames.add(method.getName())) {
                uniquePaymentMethods.add(method);
            }
        }

        PaymentMethodAdapter paymentMethodAdapter = new PaymentMethodAdapter(uniquePaymentMethods);
        paymentMethodAdapter.setOnPaymentMethodSelectedListener(paymentMethod -> {
            selectedPaymentMethod = paymentMethod;
            selectedPaymentMethodEnum = PaymentMethod.Bank_Transfer;
            Toast.makeText(this, "Selected: " + paymentMethod.getName(), Toast.LENGTH_SHORT).show();
        });

        paymentMethodsRecyclerView.setHasFixedSize(true);
        paymentMethodsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        paymentMethodsRecyclerView.setAdapter(paymentMethodAdapter);
    }


    private void handlePaymentCompletion() {
        completePaymentButton.setOnClickListener(v -> {
            if (selectedPaymentMethod == null) {
                Toast.makeText(this, "Please select payment method\n", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!termsCheckbox.isChecked()) {
                Toast.makeText(this, "Please agree to the terms\n", Toast.LENGTH_SHORT).show();
                return;
            }

            sendBookingRequest();

            Intent intent = new Intent(this, vn.edu.usth.mcma.frontend.MainActivity.class);
            intent.putExtra(IntentKey.navigate_to.name(), "HomeFragment");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void sendBookingRequest() {
        BookingRequest.Builder builder = new BookingRequest.Builder(
                movieId, selectedCityId, selectedCinemaId, selectedScreenId, selectedScheduleId,
                selectedTicketIds, selectedSeatIds);

        if (!selectedFoodIds.isEmpty()) {
            builder.setFoodIds(selectedFoodIds);
        }
        if (!selectedDrinkIds.isEmpty()) {
            builder.setDrinkIds(selectedDrinkIds);
        }
        if (selectedMovieCouponId != 0 && selectedMovieCouponId > 0) {
            builder.setMovieCouponId(selectedMovieCouponId);
        }
        if (selectedUserCouponId != 0 && selectedUserCouponId > 0) {
            builder.setUserCouponId(selectedUserCouponId);
        }

        // Tạo đối tượng BookingRequest
        BookingRequest bookingRequest = builder.build();

        // Tạo đối tượng API
        ApiService
                .getBookingApi(this)
                .processBooking(bookingRequest)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<SendBookingResponse> call, @NonNull Response<SendBookingResponse> response) {
                        if (response.isSuccessful()) {
                            SendBookingResponse sendBookingResponse = response.body();
                            if (sendBookingResponse != null) {
                                Toast.makeText(getApplicationContext(), "Process Booking successfully!", Toast.LENGTH_SHORT).show();
                                int bookingId = sendBookingResponse.getBookingId();
                                completeBooking(bookingId, selectedPaymentMethodEnum);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to Process Booking.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SendBookingResponse> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void completeBooking(int bookingId, PaymentMethod paymentMethod) {
        BookingRequest bookingRequest = new BookingRequest(bookingId, paymentMethod);

        ApiService
                .getBookingApi(this)
                .completeBooking(bookingRequest)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<BookingResponse> call, @NonNull Response<BookingResponse> response) {
                        if (response.isSuccessful()) {
                            BookingResponse bookingResponse = response.body();
                            if (bookingResponse != null) {
                                if (paymentMethod.equals(PaymentMethod.Bank_Transfer)) {
                                    Toast.makeText(getApplicationContext(), "Complete Booking successfully!", Toast.LENGTH_SHORT).show();
                                    if (ContextCompat.checkSelfPermission(PayingMethodActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                                        bookingMessageSuccessFully = String.format(
                                                """
                                                        Your booking for the movie "%s" is confirmed!
                                                        Booking Number: %s
                                                        Date & Time: %s - %s
                                                        Cinema: %s, %s
                                                        Screen: %s
                                                        Tickets: %s
                                                        Seats: %s
                                                        Please confirm your booking by calling our hotline: 1234567890 or visiting our website.
                                                        Thank you for booking with us!""",
                                                """
                                                        Enjoy your movie!
                                                        Best Regards,
                                                        SpotCinema+""",
                                                bookingResponse.getMovieName(),
                                                bookingResponse.getBookingNo(),
                                                bookingResponse.getStartDateTime(),
                                                bookingResponse.getEndDateTime(),
                                                bookingResponse.getCinemaName(),
                                                bookingResponse.getCityName(),
                                                bookingResponse.getScreenName(),
                                                String.join(", ", bookingResponse.getTicketTypeName()),
                                                String.join(", ", bookingResponse.getSeatName())
                                        );
                                        sendSMS(bookingMessageSuccessFully);
                                    } else {
                                        ActivityCompat.requestPermissions(PayingMethodActivity.this,
                                                new String[]{Manifest.permission.SEND_SMS},
                                                100);
                                    }
                                } else if (paymentMethod.equals(PaymentMethod.Cash)) {
                                    Toast.makeText(getApplicationContext(), "Your Booking Status will be Pending, and your seat(s) will be held.", Toast.LENGTH_LONG).show();
                                    Toast.makeText(getApplicationContext(), "Please go to your selected cinema to pay  for your booking before the start date of the movie!", Toast.LENGTH_LONG).show();
                                    Toast.makeText(getApplicationContext(), "Or else your booking will be canceled!!", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to Complete Booking.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BookingResponse> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults. length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendSMS(bookingMessageSuccessFully);
        } else {
            Toast.makeText(this, "Permission Denied !! ", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS(String message) {
        if (Register_Activity.phoneNumber != 0){
            SmsManager smsManager = SmsManager.getDefault();

            smsManager.sendTextMessage(String.valueOf(Register_Activity.phoneNumber), null, message, null, null);
            Toast.makeText(this, "SMS Sent Successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Please try again!", Toast.LENGTH_SHORT).show();
        }
    }
}
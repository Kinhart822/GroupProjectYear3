package com.spring.dto.response.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Integer bookingId;
    private String bookingNo;
    private Integer movieId;
    private String movieName;
    private List<String> movieGenre;
    private String imageUrlMovie;
    private String cityName;
    private String cinemaName;
    private String startDateTime;
    private String endDateTime;
    private String screenName;
    private List<String> ticketTypeName;
    private List<String> seatName;
    private List<String> foodNameList;
    private List<String> drinkNameList;
    private List<String> couponName;
    private Double totalPrice;
    private String status;
}

package com.spring.dto.response.booking.bookingSelected;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SelectedSeatsResponse {
    private List<String> selectedSeats;
    private List<String> selectedSeatTypeList;
    private List<Double> seatPriceList;
    private Double totalPrice;
}

package vn.edu.usth.mcma.backend.service;

import constants.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.usth.mcma.backend.dto.*;
import vn.edu.usth.mcma.backend.entity.*;
import vn.edu.usth.mcma.backend.exception.BusinessException;
import vn.edu.usth.mcma.backend.repository.*;

import java.time.Instant;
import java.util.*;

@Transactional
@Service
@AllArgsConstructor
public class BookingService {
    private final ScheduleRepository scheduleRepository;
    private final ReviewRepository reviewRepository;
    private final CityRepository cityRepository;
    private final CinemaRepository cinemaRepository;
    private final ScreenRepository screenRepository;
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;
    private MovieRepository movieRepository;
    public MoviePresentation getAllInformationOfSelectedMovie(Long movieId) {
        Movie movie = movieRepository
                .findById(movieId)
                .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND));
        // genres
        List<GenrePresentation> genres = movie
                .getGenreSet()
                .stream()
                .map(g -> GenrePresentation
                        .builder()
                        .id(g.getId())
                        .name(g.getName())
                        .description(g.getDescription())
                        .imageUrl(g.getImageUrl())
                        .build())
                .toList();
        // performers
        List<PerformerPresentation> performers = movie
                .getPerformerSet()
                .stream()
                .map(p -> PerformerPresentation
                        .builder()
                        .id(p.getId())
                        .name(p.getName())
                        .type(PerformerType.getById(p.getTypeId()).name())
                        .dob(p.getDateOfBirth().toString())
                        .sex(Sex.getById(p.getSex()).name())
                        .build())
                .toList();
        // rating
        Rating r = movie.getRating();
        RatingPresentation rating = RatingPresentation
                .builder()
                .id(r.getId())
                .name(r.getName())
                .description(r.getDescription())
                .build();
        // reviews
        List<ReviewPresentation> reviews = reviewRepository
                .findAllByMovieAndStatusIs(movie, CommonStatus.ACTIVE.getStatus())
                .stream()
                .map(review -> ReviewPresentation
                        .builder()
                        .id(review.getId())
                        .userComment(review.getUserComment())
                        .userVote(review.getUserVote())
                        .build())
                .toList();
        // schedules
        List<SchedulePresentation> schedules = scheduleRepository
                .findAllByMovieAndStartTimeIsAfterAndStatusIs(movie, Instant.now(), CommonStatus.ACTIVE.getStatus())
                .stream()
                .map(s -> SchedulePresentation
                        .builder()
                        .id(s.getId())
                        .screenId(s.getScreen().getId())
                        .movieId(s.getMovie().getId())
                        .startTime(s.getStartTime().toString())
                        .endTime(s.getEndTime().toString())
                        .build())
                .toList();
        return MoviePresentation
                .builder()
                .id(movie.getId())
                .name(movie.getName())
                .length(movie.getLength())
                .description(movie.getDescription())
                .publishDate(movie.getPublishDate().toString().substring(0,10))
                .trailerUrl(movie.getTrailerUrl())
                .imageUrl(movie.getImageUrl())
                .backgroundImageUrl(movie.getBackgroundImageUrl())
                .schedules(schedules)
                .genres(genres)
                .performers(performers)
                .rating(rating)
                .reviews(reviews)
                .build();
    }
    public List<CityPresentation> getAllCitiesBySelectedMovie(Long movieId) {
        Movie movie = movieRepository
                .findById(movieId)
                .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND));
        return scheduleRepository
                .findAllByMovieAndStartTimeIsAfterAndStatusIs(movie, Instant.now(), CommonStatus.ACTIVE.getStatus())
                .stream()
                .map(Schedule::getScreen)
                .map(Screen::getCinema)
                .map(Cinema::getCity)
                .map(c -> CityPresentation
                        .builder()
                        .cityId(c.getId())
                        .cityName(c.getName())
                        .build())
                .toList();
    }
    public List<CinemaPresentation> getAllCinemasBySelectedMovieAndSelectedCity(Long movieId, Long cityId) {
        City city = cityRepository
                .findById(cityId)
                .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND));
        Movie movie = movieRepository
                .findById(movieId)
                .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND));
        return scheduleRepository
                .findAllByMovieAndStartTimeIsAfterAndStatusIs(movie, Instant.now(), CommonStatus.ACTIVE.getStatus())
                .stream()
                .map(Schedule::getScreen)
                .map(Screen::getCinema)
                .filter(c -> Objects.equals(c.getCity().getId(), city.getId()))
                .map(c -> CinemaPresentation
                        .builder()
                        .id(c.getId())
                        .name(c.getName())
                        .address(c.getAddress())
                        .build())
                .toList();
    }
    public List<ScreenPresentation> getAllScreensBySelectedCinema(Long cinemaId) {
        Cinema cinema = cinemaRepository
                .findById(cinemaId)
                .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND));
        return screenRepository
                .findAllByCinema(cinema)
                .stream()
                .map(s -> ScreenPresentation
                        .builder()
                        .id(s.getId())
                        .name(s.getName())
                        .build())
                .toList();
    }
    public List<SchedulePresentation> getAllSchedulesByMovieAndScreen(Long movieId, Long screenId) {
        Movie movie = movieRepository
                .findById(movieId)
                .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND));
        Screen screen = screenRepository
                .findById(screenId)
                .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND));
        return scheduleRepository
                .findAllByMovieAndScreenAndStartTimeIsAfterAndStatusIs(movie, screen, Instant.now(), CommonStatus.ACTIVE.getStatus())
                .stream()
                .map(s -> SchedulePresentation
                        .builder()
                        .id(s.getId())
                        .screenId(s.getScreen().getId())
                        .movieId(s.getMovie().getId())
                        .startTime(s.getStartTime().toString())
                        .endTime(s.getEndTime().toString())
                        .build())
                .toList();
    }
    public List<SeatPresentation> getAllSeatsByScreenId(Long screenId) {
        return seatRepository
                .findAllByScreenId(screenId)
                .stream()
                .map(s -> SeatPresentation
                        .builder()
                        .row(s.getPk().getRow())
                        .col(s.getPk().getCol())
                        .typeId(s.getTypeId())
                        .name(s.getName())
                        .rootRow(s.getRootRow())
                        .rootCol(s.getRootCol())
                        .availability(s.getAvailability().ordinal())
                        .build())
                .toList();
    }
//    public List<TicketResponse> getAllTickets() {
//        List<Ticket> tickets = ticketRepository.findAll();
//        List<TicketResponse> ticketResponses = new ArrayList<>();
//
//        for (Ticket ticket : tickets) {
//            List<Integer> ticketsIds = new ArrayList<>();
//            List<String> ticketTypes = new ArrayList<>();
//            List<String> ticketDescriptions = new ArrayList<>();
//            List<Double> ticketPriceList = new ArrayList<>();
//
//            ticketsIds.add(ticket.getId());
//            ticketTypes.add(ticket.getTicketType().getName());
//            ticketDescriptions.add(ticket.getTicketType().getDescription());
//            ticketPriceList.add(ticket.getTicketType().getPrice());
//
//            TicketResponse response = new TicketResponse(ticketsIds, ticketTypes, ticketDescriptions, ticketPriceList);
//            ticketResponses.add(response);
//        }
//        return ticketResponses;
//    }
//    public List<UnavailableSeatResponse> getAllUnavailableSeatsBySelectedScreen(Integer screenId) {
//        Screen screen = screenRepository.findById(screenId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid screen"));
//
//        List<Seat> unAvailableSeats = seatRepository.findBySeatStatusAndScreenId(SeatStatus.Unavailable, screen.getId());
//        List<UnavailableSeatResponse> unavailableSeatResponses = new ArrayList<>();
//
//        for (Seat seat : unAvailableSeats) {
//            UnavailableSeatResponse unavailableSeatResponse = new UnavailableSeatResponse(
//                    screen.getName(),
//                    seat.getId(),
//                    seat.getName(),
//                    seat.getSeatStatus().toString(),
//                    seat.getColumn(),
//                    seat.getRow(),
//                    seat.getSeatType().getName()
//            );
//            unavailableSeatResponses.add(unavailableSeatResponse);
//        }
//
//        return unavailableSeatResponses;
//    }
//
//
//    public List<HeldSeatResponse> getAllHeldSeatsBySelectedScreen(Integer screenId) {
//        Screen screen = screenRepository.findById(screenId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid screen"));
//
//        List<Seat> heldSeats = seatRepository.findBySeatStatusAndScreenId(SeatStatus.Held, screen.getId());
//        List<HeldSeatResponse> heldSeatResponses = new ArrayList<>();
//
//        for (Seat seat : heldSeats) {
//            HeldSeatResponse heldSeatResponse = new HeldSeatResponse(
//                    screen.getName(),
//                    seat.getId(),
//                    seat.getName(),
//                    seat.getSeatStatus().toString(),
//                    seat.getColumn(),
//                    seat.getRow(),
//                    seat.getSeatType().getName()
//            );
//            heldSeatResponses.add(heldSeatResponse);
//        }
//
//        return heldSeatResponses;
//    }
//
//
//    public List<AvailableSeatResponse> getAllAvailableSeatsBySelectedScreen(Integer screenId) {
//        Screen screen = screenRepository.findById(screenId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid screen"));
//
//        List<Seat> availableSeats = seatRepository.findBySeatStatusAndScreenId(SeatStatus.Available, screen.getId());
//        if (availableSeats == null || availableSeats.isEmpty()) {
//            throw new IllegalArgumentException("No available seats found for given screen.");
//        }
//        List<AvailableSeatResponse> seatResponses = new ArrayList<>();
//
//        for (Seat seat : availableSeats) {
//            AvailableSeatResponse seatResponse = new AvailableSeatResponse(
//                    screen.getName(),
//                    seat.getId(),
//                    seat.getName(),
//                    seat.getSeatStatus().toString(),
//                    seat.getColumn(),
//                    seat.getRow(),
//                    seat.getSeatType().getName(),
//                    seat.getSeatType().getPrice()
//            );
//            seatResponses.add(seatResponse);
//        }
//
//        return seatResponses;
//    }
//
//
//    public List<ListFoodAndDrinkToOrderingResponse> getAllFoodsAndDrinksByCinema(Integer cinemaId) {
//        Cinema cinema = cinemaRepository.findById(cinemaId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid cinema"));
//
//        List<Food> foods = foodRepository.findByCinema(cinema);
//        if (foods == null || foods.isEmpty()) {
//            throw new IllegalArgumentException("No foods found for given cinema.");
//        }
//        List<Drink> drinks = drinkRepository.findByCinema(cinema);
//        if (drinks == null || drinks.isEmpty()) {
//            throw new IllegalArgumentException("No drinks found for given cinema.");
//        }
//
//        List<Integer> foodIds = new ArrayList<>();
//        List<String> foodNameList = new ArrayList<>();
//        List<String> imageUrlFoodList = new ArrayList<>();
//        List<String> descriptionFoodList = new ArrayList<>();
//        List<SizeFoodOrDrink> sizeFoodList = new ArrayList<>();
//        List<Double> foodPriceList = new ArrayList<>();
//
//        List<Integer> drinkIds = new ArrayList<>();
//        List<String> drinkNameList = new ArrayList<>();
//        List<String> imageUrlDrinkList = new ArrayList<>();
//        List<String> descriptionDrinkList = new ArrayList<>();
//        List<SizeFoodOrDrink> sizeDrinkList = new ArrayList<>();
//        List<Double> drinkPriceList = new ArrayList<>();
//
//        for (Food food : foods) {
//            foodIds.add(food.getId());
//            foodNameList.add(food.getName());
//            imageUrlFoodList.add(food.getImageUrl());
//            descriptionFoodList.add(food.getDescription());
//            sizeFoodList.add(food.getSize());
//            foodPriceList.add(food.getPrice());
//        }
//
//        for (Drink drink : drinks) {
//            drinkIds.add(drink.getId());
//            drinkNameList.add(drink.getName());
//            imageUrlDrinkList.add(drink.getImageUrl());
//            descriptionDrinkList.add(drink.getDescription());
//            sizeDrinkList.add(drink.getSize());
//            drinkPriceList.add(drink.getPrice());
//        }
//
//        ListFoodAndDrinkToOrderingResponse listFoodAndDrinkToOrderingResponse = new ListFoodAndDrinkToOrderingResponse(
//                cinema.getName(),
//                foodIds,
//                foodNameList,
//                imageUrlFoodList,
//                descriptionFoodList,
//                sizeFoodList,
//                foodPriceList,
//                drinkIds,
//                drinkNameList,
//                imageUrlDrinkList,
//                descriptionDrinkList,
//                sizeDrinkList,
//                drinkPriceList
//        );
//
//        return List.of(listFoodAndDrinkToOrderingResponse);
//    }
//
//
//    public List<CouponResponse> getAllCouponsByUser(Integer userId) {
//        List<Coupon> availableUserCouponIds = couponRepository.findAvailableCouponsForUser(userId);
//        if (availableUserCouponIds == null || availableUserCouponIds.isEmpty()) {
//            throw new IllegalArgumentException("No available coupons found for given user.");
//        }
//
//        availableUserCouponIds.removeIf(coupon -> coupon.getDateAvailable().after(new Date()) || coupon.getDateExpired().before(new Date()));
//
//        List<Integer> couponIds = new ArrayList<>();
//        List<String> couponNameList = new ArrayList<>();
//        List<String> couponDescriptionList = new ArrayList<>();
//        List<BigDecimal> discountRateList = new ArrayList<>();
//        List<Integer> minSpendReqList = new ArrayList<>();
//        List<Integer> discountLimitList = new ArrayList<>();
//
//        for (Coupon coupon : availableUserCouponIds) {
//            couponIds.add(coupon.getId());
//            couponNameList.add(coupon.getName());
//            couponDescriptionList.add(coupon.getDescription());
//            discountRateList.add(coupon.getDiscount());
//            minSpendReqList.add(coupon.getMinSpendReq());
//            discountLimitList.add(coupon.getDiscountLimit());
//        }
//
//        CouponResponse couponResponse = new CouponResponse(
//                couponIds,
//                couponNameList,
//                couponDescriptionList,
//                discountRateList,
//                minSpendReqList,
//                discountLimitList
//        );
//
//        return List.of(couponResponse);
//    }
//
//
//    public List<CouponResponse> getAllCouponsByMovie(Integer movieId) {
//        List<Coupon> availableMovieCouponIds = couponRepository.findAvailableCouponsByMovieId(movieId);
//        if (availableMovieCouponIds == null || availableMovieCouponIds.isEmpty()) {
//            throw new IllegalArgumentException("No available coupons found for given movie.");
//        }
//
//        availableMovieCouponIds.removeIf(coupon -> coupon.getDateAvailable().after(new Date()) || coupon.getDateExpired().before(new Date()));
//
//        List<Integer> couponIds = new ArrayList<>();
//        List<String> couponNameList = new ArrayList<>();
//        List<String> couponDescriptionList = new ArrayList<>();
//        List<BigDecimal> discountRateList = new ArrayList<>();
//        List<Integer> minSpendReqList = new ArrayList<>();
//        List<Integer> discountLimitList = new ArrayList<>();
//
//        for (Coupon coupon : availableMovieCouponIds) {
//            couponIds.add(coupon.getId());
//            couponNameList.add(coupon.getName());
//            couponDescriptionList.add(coupon.getDescription());
//            discountRateList.add(coupon.getDiscount());
//            minSpendReqList.add(coupon.getMinSpendReq());
//            discountLimitList.add(coupon.getDiscountLimit());
//        }
//
//        CouponResponse couponResponse = new CouponResponse(
//                couponIds,
//                couponNameList,
//                couponDescriptionList,
//                discountRateList,
//                minSpendReqList,
//                discountLimitList
//        );
//
//        return List.of(couponResponse);
//    }
//
//
//    public SendBookingResponse processingBooking(Integer userId, BookingRequest bookingRequest) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user"));
//
//        Booking booking = new Booking();
//        booking.setUser(user);
//
//        LocalDate now = LocalDate.now();
//        LocalDate maxAllowedDate = now.plusDays(4);
//
//        Movie movie = movieRepository.findById(bookingRequest.getMovieId())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid movie"));
//
//        LocalDate datePublish = movie.getDatePublish().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        if (datePublish.isAfter(maxAllowedDate)) {
//            throw new IllegalArgumentException("The movie cannot be selected because it have a schedule set more than a 4 days into the future.");
//        }
//        booking.setMovie(movie);
//
//        City city = cityRepository.findById(bookingRequest.getCityId())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid city"));
//        if (city.getMovie().getId() != movie.getId()) {
//            throw new IllegalArgumentException("The selected city does not match the specified movie!");
//        }
//        booking.setCity(city);
//
//        Cinema cinema = cinemaRepository.findById(bookingRequest.getCinemaId())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid cinema"));
//        if (cinema.getCity().getId() != city.getId()) {
//            throw new IllegalArgumentException("The selected cinema does not match the specified city!");
//        }
//        booking.setCinema(cinema);
//
//        Screen screen = screenRepository.findById(bookingRequest.getScreenId())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid screen"));
//        if (screen.getCinema().getId() != cinema.getId()) {
//            throw new IllegalArgumentException("The selected screen does not match the specified cinema!");
//        }
//        booking.setScreen(screen);
//
//        MovieSchedule selectedSchedule = movieScheduleRepository.findById(bookingRequest.getScheduleId())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid movie schedule"));
//        if (selectedSchedule.getMovie().getId() != movie.getId() && selectedSchedule.getCinema().getId() != cinema.getId()
//                && selectedSchedule.getScreen().getId() != screen.getId()) {
//            throw new IllegalArgumentException("The selected schedule does not match the specified cinema!");
//        }
//
//        LocalDateTime currentTime = LocalDateTime.now();
//        LocalDateTime movieStartTime = selectedSchedule.getStartTime();
//        LocalDateTime movieStartTimePlus10Minutes = movieStartTime.plusMinutes(10);
//
//        if (currentTime.isAfter(movieStartTime) && currentTime.isAfter(movieStartTimePlus10Minutes)) {
//            throw new IllegalArgumentException("Cannot book a ticket after the movie has started for more than 10 minutes.");
//        }
//
//        LocalDateTime startDateTime = selectedSchedule.getStartTime();
//        int lengthInMinutes = movie.getLength();
//        long lengthInSeconds = lengthInMinutes * 60L;
//        Duration movieDuration = Duration.ofSeconds(lengthInSeconds);
//        LocalDateTime endDateTime = startDateTime.plus(movieDuration);
//
//        booking.setStartDateTime(startDateTime);
//        booking.setEndDateTime(endDateTime);
//        booking.setMovieSchedule(selectedSchedule);
//
//        if (bookingRequest.getTicketIds() != null && !bookingRequest.getTicketIds().isEmpty()) {
//            Map<Integer, BookingTicket> ticketMap = new HashMap<>();
//
//            for (Integer ticketId : bookingRequest.getTicketIds()) {
//                Ticket ticket = ticketRepository.findById(ticketId)
//                        .orElseThrow(() -> new IllegalArgumentException("Invalid ticket ID"));
//
//                BookingTicket bookingTicket = ticketMap.computeIfAbsent(ticketId, id -> {
//                    BookingTicket newBookingTicket = new BookingTicket();
//                    newBookingTicket.setBooking(booking);
//                    newBookingTicket.setTicket(ticket);
//                    newBookingTicket.setQuantity(0);
//                    return newBookingTicket;
//                });
//
//                bookingTicket.setQuantity(bookingTicket.getQuantity() + 1);
//            }
//            booking.setTickets(new ArrayList<>(ticketMap.values()));
//        }
//
//        double totalPrice = booking.getTickets().stream()
//                .mapToDouble(bookingTicket -> bookingTicket.getTicket().getTicketType().getPrice() * bookingTicket.getQuantity())
//                .sum();
//        booking.setTotalPrice(totalPrice);
//
//        int ticketCount = booking.getTickets().stream()
//                .mapToInt(BookingTicket::getQuantity)
//                .sum();
//        int seatCount = bookingRequest.getSeatIds().size();
//        if (seatCount != ticketCount) {
//            throw new IllegalArgumentException("The number of selected seats (%d) does not match the number of tickets (%d).".formatted(seatCount, ticketCount));
//        }
//
//        Set<Integer> seatIdSet = new HashSet<>(bookingRequest.getSeatIds());
//        if (seatIdSet.size() != bookingRequest.getSeatIds().size()) {
//            throw new IllegalArgumentException("Duplicate seat IDs detected in the request.");
//        }
//
//        List<Seat> selectedSeats = seatRepository.findAllById(bookingRequest.getSeatIds())
//                .stream()
//                .toList();
//        if (selectedSeats.size() != seatCount) {
//            throw new IllegalArgumentException("One or more selected seats are unavailable.");
//        }
//        if (selectedSeats.stream().anyMatch(seat -> seat.getScreen().getId() != screen.getId())) {
//            throw new IllegalArgumentException("The selected seats do not match the specified screen!");
//        }
//        if (selectedSeats.stream().anyMatch(seat -> seat.getSeatStatus() == SeatStatus.Unavailable)) {
//            throw new IllegalArgumentException("Selected seats are already unavailable, please select a different seat.");
//        }
//        if (selectedSeats.stream().anyMatch(seat -> seat.getSeatStatus() == SeatStatus.Held)) {
//            throw new IllegalArgumentException("Selected seats are already held, please select a different seat.");
//        }
//
//        if (bookingRequest.getSeatIds() != null && !bookingRequest.getSeatIds().isEmpty()) {
//            Map<Integer, BookingSeat> seatMap = new HashMap<>();
//
//            for (Seat seat : selectedSeats) {
//                BookingSeat bookingSeat = seatMap.computeIfAbsent(seat.getId(), id -> {
//                    BookingSeat newBookingSeat = new BookingSeat();
//                    newBookingSeat.setBooking(booking);
//                    newBookingSeat.setSeat(seat);
//                    newBookingSeat.setSeatType(seat.getSeatType());
//                    return newBookingSeat;
//                });
//                seat.setSeatStatus(SeatStatus.Held);
//            }
//            booking.setSeatList(new ArrayList<>(seatMap.values()));
//        }
//
//        double totalPriceSeat = booking.getSeatList().stream()
//                .mapToDouble(draftSeat -> draftSeat.getSeatType().getPrice())
//                .sum();
//        double updateTotalPrice = booking.getTotalPrice() + totalPriceSeat;
//        booking.setTotalPrice(updateTotalPrice);
//
////        if (bookingRequest.getFoodIds() != null && !bookingRequest.getFoodIds().isEmpty()) {
////            Map<String, BookingFood> foodMap = new HashMap<>();
////            double totalFoodPrice = 0.0;
////
////            for (int i = 0; i < bookingRequest.getFoodIds().size(); i++) {
////                Integer foodId = bookingRequest.getFoodIds().get(i);
////                SizeFoodOrDrink size = bookingRequest.getSizeFoodList().get(i);
////
////                Food food = foodRepository.findById(foodId)
////                        .orElseThrow(() -> new IllegalArgumentException("Food not found"));
////                if (food.getCinema().getId() != cinema.getId()) {
////                    throw new IllegalArgumentException("The selected food does not match the specified cinema!");
////                }
////
////                String key = "%d-%s".formatted(foodId, size.name());
////
////                BookingFood bookingFood = foodMap.computeIfAbsent(key, k -> {
////                    BookingFood newBookingFood = new BookingFood();
////                    newBookingFood.setBooking(booking);
////                    newBookingFood.setFood(food);
////                    newBookingFood.setSizeFood(size);
////                    newBookingFood.setQuantity(1);
////                    return newBookingFood;
////                });
////
////                bookingFood.setQuantity(bookingFood.getQuantity() + 1);
//////                double price = food.getPrice() * getFoodOrDrinkSize(size) * bookingFood.getQuantity();
////                double price = food.getPrice() * bookingFood.getQuantity();
////                totalFoodPrice += price;
////            }
////
////            updateTotalPrice = booking.getTotalPrice() + totalFoodPrice;
////            booking.setTotalPrice(updateTotalPrice);
////
////            booking.setFoodList(new ArrayList<>(foodMap.values()));
////        }
//
//        if (bookingRequest.getFoodIds() != null && !bookingRequest.getFoodIds().isEmpty()) {
//            Map<Integer, BookingFood> foodMap = new HashMap<>();
//            double totalFoodPrice = 0.0;
//
//            for (Integer foodId : bookingRequest.getFoodIds()) {
//                Food food = foodRepository.findById(foodId)
//                        .orElseThrow(() -> new IllegalArgumentException("Food not found"));
//                if (food.getCinema().getId() != cinema.getId()) {
//                    throw new IllegalArgumentException("The selected food does not match the specified cinema!");
//                }
//
//                BookingFood bookingFood = foodMap.computeIfAbsent(foodId, k -> {
//                    BookingFood newBookingFood = new BookingFood();
//                    newBookingFood.setBooking(booking);
//                    newBookingFood.setFood(food);
//                    newBookingFood.setQuantity(0);
//                    return newBookingFood;
//                });
//
//                bookingFood.setQuantity(bookingFood.getQuantity() + 1);
//            }
//
//            for (BookingFood bookingFood : foodMap.values()) {
//                Food food = bookingFood.getFood();
//                double price = food.getPrice() * bookingFood.getQuantity();
//                totalFoodPrice += price;
//            }
//
//            updateTotalPrice = booking.getTotalPrice() + totalFoodPrice;
//            booking.setTotalPrice(updateTotalPrice);
//
//            booking.setFoodList(new ArrayList<>(foodMap.values()));
//        }
//
////        if (bookingRequest.getDrinkIds() != null && !bookingRequest.getDrinkIds().isEmpty()) {
////            Map<String, BookingDrink> drinkMap = new HashMap<>();
////            double totalDrinkPrice = 0.0;
////
////            for (int i = 0; i < bookingRequest.getDrinkIds().size(); i++) {
////                Integer drinkId = bookingRequest.getDrinkIds().get(i);
////                SizeFoodOrDrink size = bookingRequest.getSizeDrinkList().get(i);
////
////                Drink drink = drinkRepository.findById(drinkId)
////                        .orElseThrow(() -> new IllegalArgumentException("Drink not found"));
////                if (drink.getCinema().getId() != cinema.getId()) {
////                    throw new IllegalArgumentException("The selected drink does not match the specified cinema!");
////                }
////
////                String key = "%d-%s".formatted(drinkId, size.name());
////
////                BookingDrink bookingDrink = drinkMap.computeIfAbsent(key, k -> {
////                    BookingDrink newBookingDrink = new BookingDrink();
////                    newBookingDrink.setBooking(booking);
////                    newBookingDrink.setDrink(drink);
////                    newBookingDrink.setSizeDrink(size);
////                    newBookingDrink.setQuantity(1);
////                    return newBookingDrink;
////                });
////
////                bookingDrink.setQuantity(bookingDrink.getQuantity() + 1);
//////                double price = drink.getPrice() * getFoodOrDrinkSize(size) * bookingDrink.getQuantity();
////                double price = drink.getPrice() * bookingDrink.getQuantity();
////                totalDrinkPrice += price;
////            }
////
////            updateTotalPrice = booking.getTotalPrice() + totalDrinkPrice;
////            booking.setTotalPrice(updateTotalPrice);
////
////            booking.setDrinks(new ArrayList<>(drinkMap.values()));
////        }
//
//        if (bookingRequest.getDrinkIds() != null && !bookingRequest.getDrinkIds().isEmpty()) {
//            Map<Integer, BookingDrink> drinkMap = new HashMap<>();
//            double totalDrinkPrice = 0.0;
//
//            for (Integer drinkId : bookingRequest.getDrinkIds()) {
//                Drink drink = drinkRepository.findById(drinkId)
//                        .orElseThrow(() -> new IllegalArgumentException("Drink not found"));
//
//                if (drink.getCinema().getId() != cinema.getId()) {
//                    throw new IllegalArgumentException("The selected drink does not match the specified cinema!");
//                }
//
//                BookingDrink bookingDrink = drinkMap.computeIfAbsent(drinkId, k -> {
//                    BookingDrink newBookingDrink = new BookingDrink();
//                    newBookingDrink.setBooking(booking);
//                    newBookingDrink.setDrink(drink);
//                    newBookingDrink.setQuantity(0);
//                    return newBookingDrink;
//                });
//
//                bookingDrink.setQuantity(bookingDrink.getQuantity() + 1);
//            }
//
//            for (BookingDrink bookingDrink : drinkMap.values()) {
//                Drink drink = bookingDrink.getDrink();
//                double price = drink.getPrice() * bookingDrink.getQuantity();
//                totalDrinkPrice += price;
//            }
//
//            updateTotalPrice = booking.getTotalPrice() + totalDrinkPrice;
//            booking.setTotalPrice(updateTotalPrice);
//
//            booking.setDrinks(new ArrayList<>(drinkMap.values()));
//        }
//
//        List<BookingCoupon> bookingCoupons = new ArrayList<>();
//        double discountAmount;
//
////         Fetch and validate selected movie coupon
//        if (bookingRequest.getMovieCouponId() != null) {
//            Coupon selectedMovieCoupon = couponRepository.findCouponByCouponId(bookingRequest.getMovieCouponId());
//            if (selectedMovieCoupon == null) {
//                throw new IllegalArgumentException("Coupon not found for movie %d.".formatted(bookingRequest.getMovieId()));
//            }
////            if (updateTotalPrice < selectedMovieCoupon.getMinSpendReq()) {
////                throw new IllegalArgumentException("Minimum spend requirement not met for coupon %d.".formatted(selectedMovieCoupon.getId()));
////            }
//
//            discountAmount = updateTotalPrice * selectedMovieCoupon.getDiscount().doubleValue();
////            discountAmount = Math.min(discountAmount, selectedMovieCoupon.getDiscountLimit());
//            updateTotalPrice -= discountAmount;
//
//            BookingCoupon bookingCoupon = new BookingCoupon();
//            bookingCoupon.setBooking(booking);
//            bookingCoupon.setCoupon(selectedMovieCoupon);
//            bookingCoupons.add(bookingCoupon);
//        }
//
////         Fetch and validate selected user coupon
//        if (bookingRequest.getUserCouponId() != null) {
//            Coupon selectedUserCoupon = couponRepository.findCouponByCouponId(bookingRequest.getUserCouponId());
//            if (selectedUserCoupon == null) {
//                throw new IllegalArgumentException("Coupon not found for user %d.".formatted(userId));
//            }
////            if (updateTotalPrice < selectedUserCoupon.getMinSpendReq()) {
////                throw new IllegalArgumentException("Minimum spend requirement not met for coupon %d.".formatted(selectedUserCoupon.getId()));
////            }
//
//            discountAmount = updateTotalPrice * selectedUserCoupon.getDiscount().doubleValue();
////            discountAmount = Math.min(discountAmount, selectedUserCoupon.getDiscountLimit());
//            updateTotalPrice -= discountAmount;
//
//            BookingCoupon bookingCoupon = new BookingCoupon();
//            bookingCoupon.setBooking(booking);
//            bookingCoupon.setCoupon(selectedUserCoupon);
//            bookingCoupons.add(bookingCoupon);
//        }
//
//        updateTotalPrice = BigDecimal.valueOf(updateTotalPrice)
//                .setScale(2, RoundingMode.HALF_UP)
//                .doubleValue();
//
//        booking.setCoupons(bookingCoupons);
//        booking.setTotalPrice(updateTotalPrice);
//        booking.setStatus(BookingStatus.In_Processing);
//        booking.setDateCreated(LocalDateTime.now());
//        booking.setDateUpdated(LocalDateTime.now());
//        bookingRepository.save(booking);
//
//        // Schedule seat release if payment is not completed
//        scheduleSeatReleaseTask(booking);
//
////        return new SendBookingResponse(
////                booking.getId(),
////                booking.getMovie().getName(),
////                booking.getCity().getName(),
////                booking.getCinema().getName(),
////                booking.getStartDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm a")),
////                booking.getEndDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm a")),
////                booking.getScreen().getName(),
////                booking.getTickets().stream().map(ticket -> ticket.getTicket().getTicketType().getName()).toList(),
////                booking.getSeatList().stream().map(seat -> "%s (%s)".formatted(seat.getSeat().getName(), seat.getSeatType().getName())).toList(),
//////                booking.getFoodList().stream().map(food -> "%s (%s)".formatted(food.getFood().getName(), food.getSizeFood())).toList(),
//////                booking.getDrinks().stream().map(drink -> "%s (%s)".formatted(drink.getDrink().getName(), drink.getSizeDrink())).toList(),
////                booking.getFoodList().stream().map(food -> "%s".formatted(food.getFood().getName())).toList(),
////                booking.getDrinks().stream().map(drink -> "%s".formatted(drink.getDrink().getName())).toList(),
////                booking.getStatus().toString()
////        );
//
//        return SendBookingResponse.builder()
//                .bookingId(booking.getId())
//                .movieName(booking.getMovie().getName())
//                .cityName(booking.getCity().getName())
//                .cinemaName(booking.getCinema().getName())
//                .startDateTime(booking.getStartDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm a")))
//                .endDateTime(booking.getEndDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm a")))
//                .screenName(booking.getScreen().getName())
//                .ticketTypeName(booking.getTickets().stream().map(ticket -> ticket.getTicket().getTicketType().getName()).toList())
//                .seatName(booking.getSeatList().stream().map(seat -> "%s (%s)".formatted(seat.getSeat().getName(), seat.getSeatType().getName())).toList())
//                .foodNameList(Optional.ofNullable(booking.getFoodList())
//                        .orElse(Collections.emptyList())
//                        .stream()
//                        .map(food -> "%s - Quantity: %d".formatted(food.getFood().getName(), food.getQuantity()))
//                        .toList())
//                .drinkNameList(Optional.ofNullable(booking.getDrinks())
//                        .orElse(Collections.emptyList())
//                        .stream()
//                        .map(drink -> "%s - Quantity: %d".formatted(drink.getDrink().getName(), drink.getQuantity()))
//                        .toList())
//                .couponName(Optional.ofNullable(booking.getCoupons())
//                        .orElse(Collections.emptyList())
//                        .stream()
//                        .map(bookingCoupon -> "%s (%s)".formatted(bookingCoupon.getCoupon().getName(), bookingCoupon.getCoupon().getDescription()))
//                        .toList())
//                .totalPrice(updateTotalPrice)
//                .bookingStatus(booking.getStatus().toString())
//                .build();
//
//    }
//
//
//    public BookingResponse completeBooking(Integer userId, BookingRequest bookingRequest) {
//        Booking booking = bookingRepository.findById(bookingRequest.getBookingId())
//                .orElseThrow(() -> new IllegalArgumentException("Booking not found for ID: %d".formatted(bookingRequest.getBookingId())));
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found for ID: %d".formatted(userId)));
//        List<MovieGenre> movieGenres = movieGenreRepository.findMovieGenresByMovie(booking.getMovie().getId());
//        if (movieGenres.isEmpty()) {
//            throw new IllegalArgumentException("Movie does not have any genre.");
//        }
//
//        if (isBookingIncomplete(booking)) {
//            deleteBooking(booking.getId(), userId);
//            throw new IllegalArgumentException("Booking is still not completed. Please fill all the booking information again!");
//        }
//
//        if (booking.getSeatList().stream().anyMatch(seat -> seat.getSeat().getSeatStatus() == SeatStatus.Unavailable)) {
//            throw new IllegalArgumentException("Some of the selected seats are already bought by another user. Please fill all the booking information again!");
//        }
//
//
//        if (booking.getSeatList().stream().anyMatch(seat -> seat.getSeat().getSeatStatus() != SeatStatus.Held)) {
//            throw new IllegalArgumentException("Seat is still not held. Please fill all the booking information again!");
//        }
//
//        booking.setBookingNo(generateBookingNo(user.getLastName(), user.getFirstName(), booking.getMovie().getName()));
//        booking.setPaymentMethod(bookingRequest.getPaymentMethod());
//        booking.setStatus(bookingRequest.getPaymentMethod() == PaymentMethod.Cash
//                ? BookingStatus.Pending_Payment
//                : BookingStatus.Confirmed);
//        if (booking.getStatus() == BookingStatus.Confirmed) {
//            booking.getSeatList().forEach(seat -> seat.getSeat().setSeatStatus(SeatStatus.Unavailable));
//        }
//
//        List<Seat> seatsToUpdate = booking.getSeatList().stream()
//                .map(BookingSeat::getSeat)
//                .collect(Collectors.toList());
//        seatRepository.saveAll(seatsToUpdate);
//
//        booking.setDateUpdated(LocalDateTime.now());
//        bookingRepository.save(booking);
//
//        if (booking.getPaymentMethod().equals(PaymentMethod.Bank_Transfer)) {
//            booking.setStatus(BookingStatus.Completed);
//        }
//
//        Notification notification = new Notification();
//        notification.setUser(user);
//        notification.setMessage("Your booking for %s is confirmed. Booking Number: %s".formatted(booking.getMovie().getName(), booking.getBookingNo()));
//        notification.setDateCreated(LocalDateTime.now());
//        notificationRepository.save(notification);
//
//        try {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm a");
//            String formattedStartDateTime = booking.getStartDateTime().format(formatter);
//            String formattedEndDateTime = booking.getEndDateTime().format(formatter);
//            String subject = "Your Movie Booking Confirmation";
//            String body = "Dear %s %s,\n\nYour booking for the movie \"%s\" is confirmed!\nBooking Number: %s\nDate & Time: %s - %s\nCinema: %s, %s\nScreen: %s\nTickets: %s\nSeats: %s\nFood: %s\nDrinks: %s\nTotal Price: $%s\n\nThank you for booking with us!\n\nBest regards,\nYour Movie Booking Team".formatted(user.getFirstName(), user.getLastName(), booking.getMovie().getName(), booking.getBookingNo(), formattedStartDateTime, formattedEndDateTime, booking.getCinema().getName(), booking.getCity().getName(), booking.getScreen().getName(), booking.getTickets().stream()
//                    .map(ticket -> ticket.getTicket().getTicketType().getName())
//                    .collect(Collectors.joining(", ")), booking.getSeatList().stream()
//                    .map(seat -> "%s (%s)".formatted(seat.getSeat().getName(), seat.getSeatType().getName()))
//                    .collect(Collectors.joining(", ")), booking.getFoodList().stream()
//                    .map(food -> "%s (%s)".formatted(food.getFood().getName(), food.getSizeFood()))
//                    .collect(Collectors.joining(", ")), booking.getDrinks().stream()
//                    .map(drink -> "%s (%s)".formatted(drink.getDrink().getName(), drink.getSizeDrink()))
//                    .collect(Collectors.joining(", ")), booking.getTotalPrice());
//            emailService.sendSimpleMailMessage(user.getEmail(), subject, body);
//        } catch (Exception e) {
//            System.out.println("Failed to send email notification. Please try again later.");
//        }
//
//        return new BookingResponse(
//                booking.getId(),
//                booking.getBookingNo(),
//                booking.getMovie().getId(),
//                booking.getMovie().getName(),
//                movieGenres.stream()
//                        .map(movieGenre -> movieGenre.getMovieGenreDetail().getName())
//                        .toList(),
//                booking.getMovie().getImageUrl(),
//                booking.getCity().getName(),
//                booking.getCinema().getName(),
//                booking.getStartDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm a")),
//                booking.getEndDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm a")),
//                booking.getScreen().getName(),
//                booking.getTickets().stream().map(ticket -> ticket.getTicket().getTicketType().getName()).toList(),
//                booking.getSeatList().stream().map(seat -> "%s (%s)".formatted(seat.getSeat().getName(), seat.getSeatType().getName())).toList(),
//                booking.getFoodList().stream().map(food -> "%s - Quantity: %d".formatted(food.getFood().getName(), food.getQuantity())).toList(),
//                booking.getDrinks().stream().map(drink -> "%s - Quantity: %d".formatted(drink.getDrink().getName(), drink.getQuantity())).toList(),
//                booking.getCoupons().stream().map(coupon -> "%s (%s)".formatted(coupon.getCoupon().getName(), coupon.getCoupon().getDescription())).toList(),
//                booking.getTotalPrice(),
//                booking.getStatus().toString()
//        );
//    }
//
////
////    public void cancelBooking(Integer bookingId, Integer userId) {
////        Booking booking = bookingRepository.findById(bookingId)
////                .orElseThrow(() -> new IllegalArgumentException("Booking not found for ID: %d".formatted(bookingId)));
////
////        User user = userRepository.findById(userId)
////                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
////
////        if (booking.getStatus() == BookingStatus.Pending_Payment) {
////            if (booking.getPaymentMethod() == PaymentMethod.Cash) {
////                List<Seat> seatListChangeToAvailable = new ArrayList<>();
////                for (BookingSeat seat : booking.getSeatList()) {
////                    seat.getSeat().setSeatStatus(SeatStatus.Available);
////                    seatListChangeToAvailable.add(seat.getSeat());
////                }
////                seatRepository.saveAll(seatListChangeToAvailable);
////
////                booking.setStatus(BookingStatus.CANCELLED);
////                bookingRepository.save(booking);
////
////                try {
////                    Notification notification = new Notification();
////                    notification.setUser(user);
////                    notification.setMessage("Booking Number: %s, Your booking for %s is canceled. You will have the money that you pay for the booking return to your wallet".formatted(booking.getBookingNo(), booking.getMovie().getName()));
////                    notification.setDateCreated(LocalDateTime.now());
////                    notificationRepository.save(notification);
////
////                    emailService.sendCancelMailMessage(user.getEmail());
////                    System.out.printf("Notification sent to %s%n", user.getEmail());
////                } catch (Exception e) {
////                    System.out.println("Failed to send email notification. Please try again later.");
////                }
////
////                // Schedule the booking deletion 10 minutes after cancellation
////                try (ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor()) {
////                    scheduler.schedule(() -> deleteBooking(bookingId, userId), 10, TimeUnit.MINUTES);
////                }
////            }
////        }
////    }
//
//
//    public void cancelBooking(Integer bookingId, Integer userId) {
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new IllegalArgumentException("Booking not found for ID: %d".formatted(bookingId)));
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
//
//        if (ChronoUnit.MINUTES.between(LocalDateTime.now(), booking.getStartDateTime()) >= 60) {
//            if (booking.getStatus() == BookingStatus.Completed) {
//                if (booking.getPaymentMethod() == PaymentMethod.Bank_Transfer) {
//                    List<Seat> seatListChangeToAvailable = new ArrayList<>();
//                    for (BookingSeat seat : booking.getSeatList()) {
//                        seat.getSeat().setSeatStatus(SeatStatus.Available);
//                        seatListChangeToAvailable.add(seat.getSeat());
//                    }
//                    seatRepository.saveAll(seatListChangeToAvailable);
//
//                    booking.setStatus(BookingStatus.CANCELLED);
//                    bookingRepository.save(booking);
//
//                    try {
//                        Notification notification = new Notification();
//                        notification.setUser(user);
//                        notification.setMessage("Booking Number: %s, Your booking for %s is canceled. You will have the money that you pay for the booking return to your wallet".formatted(booking.getBookingNo(), booking.getMovie().getName()));
//                        notification.setDateCreated(LocalDateTime.now());
//                        notificationRepository.save(notification);
//
//                        emailService.sendCancelMailMessage(user.getEmail());
//                        System.out.printf("Notification sent to %s%n", user.getEmail());
//                    } catch (Exception e) {
//                        System.out.println("Failed to send email notification. Please try again later.");
//                    }
//
//                    // Schedule the booking deletion 10 minutes after cancellation
//                    try (ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor()) {
//                        scheduler.schedule(() -> deleteAfterCancelBooking(bookingId, userId), 10, TimeUnit.MINUTES);
//                    }
//                }
//            }
//        } else {
//            System.out.println("Can't cancel this booking! Please try again later.");
//        }
//    }
//
//
//    public void revokeCancelBooking(Integer bookingId, Integer userId) {
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new IllegalArgumentException("Booking not found for ID: %d".formatted(bookingId)));
//
//        // Check if the booking has already been deleted or if the cancellation cannot be revoked
//        if (!booking.getStatus().equals(BookingStatus.CANCELLED)) {
//            throw new IllegalArgumentException("Cannot revoke the cancellation. Booking is not in the CANCELLED state or has already been deleted.");
//        }
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
//
//        if (booking.getStatus().equals(BookingStatus.CANCELLED)) {
//            List<Seat> seatListChangeToHeld = new ArrayList<>();
//            for (BookingSeat seat : booking.getSeatList()) {
//                seat.getSeat().setSeatStatus(SeatStatus.Unavailable);
//                seatListChangeToHeld.add(seat.getSeat());
//            }
//            seatRepository.saveAll(seatListChangeToHeld);
//
//            booking.setStatus(BookingStatus.Completed);
//            bookingRepository.save(booking);
//
//            try {
//                Notification notification = new Notification();
//                notification.setUser(user);
//                notification.setMessage("Your booking for %s has been successfully reinstated. Booking Number: %s".formatted(booking.getMovie().getName(), booking.getBookingNo()));
//                notification.setDateCreated(LocalDateTime.now());
//                notificationRepository.save(notification);
//
//                emailService.sendReinstateMailMessage(user.getEmail());
//                System.out.printf("Notification sent to %s%n", user.getEmail());
//            } catch (Exception e) {
//                System.out.println("Failed to send email notification. Please try again later.");
//            }
//        }
//    }
//
//
//    public void deleteAfterCancelBooking(Integer bookingId, Integer userId) {
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new IllegalArgumentException("Booking not found for ID: %d".formatted(bookingId)));
//
////        if (booking.getStartDateTime().isBefore(LocalDateTime.now().plusHours(1))) {
////            throw new IllegalArgumentException("You cannot delete a booking within 1 hour of the start time.");
////        }
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
//
//        List<Seat> seatListChangeToAvailable = new ArrayList<>();
//        for (BookingSeat seat : booking.getSeatList()) {
//            seat.getSeat().setSeatStatus(SeatStatus.Available);
//            seatListChangeToAvailable.add(seat.getSeat());
//        }
//        seatRepository.saveAll(seatListChangeToAvailable);
//
//        for (BookingTicket bookingTicket : booking.getTickets()) {
//            bookingTicket.setTicket(null);
//            bookingTicketRepository.delete(bookingTicket);
//        }
//
//        bookingRepository.delete(booking);
//
//        try {
//            Notification notification = new Notification();
//            notification.setUser(user);
//            notification.setMessage("Your booking for %s is deleted. Booking Number: %s".formatted(booking.getMovie().getName(), booking.getBookingNo()));
//            notification.setDateCreated(LocalDateTime.now());
//            notificationRepository.save(notification);
//
//            emailService.sendDeleteMailMessage(user.getEmail());
//        } catch (Exception e) {
//            System.out.println("Failed to send email notification. Please try again later.");
//        }
//    }
//
//
//    public void deleteBooking(Integer bookingId, Integer userId) {
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new IllegalArgumentException("Booking not found for ID: %d".formatted(bookingId)));
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
//
//        if (ChronoUnit.MINUTES.between(LocalDateTime.now(), booking.getStartDateTime()) >= 60) {
//            if (booking.getStatus() == BookingStatus.Completed) {
//                if (booking.getPaymentMethod() == PaymentMethod.Bank_Transfer) {
//                    List<Seat> seatListChangeToAvailable = new ArrayList<>();
//                    for (BookingSeat seat : booking.getSeatList()) {
//                        seat.getSeat().setSeatStatus(SeatStatus.Available);
//                        seatListChangeToAvailable.add(seat.getSeat());
//                    }
//                    seatRepository.saveAll(seatListChangeToAvailable);
//
//                    for (BookingTicket bookingTicket : booking.getTickets()) {
//                        bookingTicket.setTicket(null);
//                        bookingTicketRepository.delete(bookingTicket);
//                    }
//
//                    bookingRepository.delete(booking);
//
//                    try {
//                        Notification notification = new Notification();
//                        notification.setUser(user);
//                        notification.setMessage("Your booking for %s is deleted. Booking Number: %s".formatted(booking.getMovie().getName(), booking.getBookingNo()));
//                        notification.setDateCreated(LocalDateTime.now());
//                        notificationRepository.save(notification);
//
//                        emailService.sendDeleteMailMessage(user.getEmail());
//                    } catch (Exception e) {
//                        System.out.println("Failed to send email notification. Please try again later.");
//                    }
//                }
//            }
//        } else {
//            System.out.println("Can't cancel this booking! Please try again later.");
//        }
//    }
//
//    // TODO: Check Xem có Update đc Seat ko (Ko cần thiết)
////
////    public void updateBookingSeat(BookingRequest bookingRequest) {
////        Booking booking = bookingRepository.findById(bookingRequest.getBookingId())
////                .orElseThrow(() -> new IllegalArgumentException("Booking not found for ID: %d".formatted(bookingRequest.getBookingId())));
////        if (bookingRequest.getSeatIds() == null || bookingRequest.getSeatIds().isEmpty()) return;
////
////        int ticketCount = booking.getTickets().stream()
////                .mapToInt(BookingTicket::getQuantity)
////                .sum();
////        int seatCount = bookingRequest.getSeatIds().size();
////        if (seatCount != ticketCount) {
////            throw new IllegalArgumentException("The number of selected seats (%d) does not match the number of tickets (%d).".formatted(seatCount, ticketCount));
////        }
////
////        List<Seat> selectedSeats = seatRepository.findAllById(bookingRequest.getSeatIds())
////                .stream()
////                .toList();
////        if (selectedSeats.size() != seatCount) {
////            throw new IllegalArgumentException("The size of the updated booking list does not match the number of the size of the old booking list");
////        }
////        if (selectedSeats.stream().anyMatch(seat -> seat.getScreen().getId() != booking.getScreen().getId())) {
////            throw new IllegalArgumentException("The selected seats do not match the specified screen!");
////        }
////        if (selectedSeats.stream().anyMatch(seat -> seat.getSeatStatus() == SeatStatus.Unavailable)) {
////            throw new IllegalArgumentException("Selected seats are already unavailable, please select a different seat.");
////        }
////
////        List<Integer> newSeatIds = selectedSeats.stream()
////                .map(Seat::getId)
////                .sorted()
////                .toList();
////        List<Integer> oldSeatIds = booking.getSeatList().stream()
////                .map(bookingSeat -> bookingSeat.getSeat().getId())
////                .toList();
////        if (newSeatIds.equals(oldSeatIds)) {
////            throw new IllegalArgumentException("The updated booking list matches the original booking list. Please make a valid change!");
////        }
////
////        // Chuyển trạng thái ghế cũ thành Available
////        List<Seat> seatListChangeToAvailable = new ArrayList<>();
////        for (BookingSeat seat : booking.getSeatList()) {
////            seat.getSeat().setSeatStatus(SeatStatus.Available);
////            seatListChangeToAvailable.add(seat.getSeat());
////        }
////        seatRepository.saveAll(seatListChangeToAvailable);
////
////        // Cập nhập ghế mới và trạng thái
////        List<BookingSeat> existingBookingSeats = booking.getSeatList();
////        for (int i = 0; i < existingBookingSeats.size(); i++) {
////            BookingSeat existingBookingSeat = existingBookingSeats.get(i);
////            Seat newSeat = selectedSeats.get(i);
////
////            existingBookingSeat.setSeat(newSeat);
////            existingBookingSeat.setSeatType(newSeat.getSeatType());
////            newSeat.setSeatStatus(SeatStatus.Held);
////        }
////
////        seatRepository.saveAll(selectedSeats);
////        booking.setSeatList(existingBookingSeats);
////
////        double newTotalPrice = booking.getSeatList().stream()
////                .mapToDouble(bookingSeat -> bookingSeat.getSeat().getSeatType().getPrice())
////                .sum();
////        booking.setTotalPrice(newTotalPrice);
////
////        bookingRepository.save(booking);
////    }
//
//    // TODO: Additional methods
//    private boolean isBookingIncomplete(Booking booking) {
//        return booking.getMovie() == null || booking.getCity() == null || booking.getCinema() == null ||
//                booking.getScreen() == null || booking.getMovieSchedule() == null || booking.getTickets().isEmpty() ||
//                booking.getSeatList().isEmpty();
//    }
//
//    private String generateBookingNo(String lastName, String firstName, String movieName) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        String datePart = LocalDate.now().format(formatter);
//        String camelCaseMovieName = convertToCamelCase(movieName);
//        String randomSuffix = String.valueOf(System.currentTimeMillis());
//
//        return "%s%s-%s-%s-%s".formatted(firstName, lastName, camelCaseMovieName, datePart, randomSuffix);
//    }
//
//    private String convertToCamelCase(String name) {
//        String[] words = name.split(" ");
//        StringBuilder camelCaseName = new StringBuilder();
//
//        if (words.length > 0) {
//            camelCaseName.append(words[0].toLowerCase());
//        }
//
//        for (int i = 1; i < words.length; i++) {
//            if (!words[i].isEmpty()) {
//                camelCaseName.append(Character.toUpperCase(words[i].charAt(0)));
//                camelCaseName.append(words[i].substring(1).toLowerCase());
//            }
//        }
//
//        return camelCaseName.toString();
//    }
//
////    private double getFoodOrDrinkSize(SizeFoodOrDrink sizeFoodOrDrink) {
////        return switch (sizeFoodOrDrink) {
////            case Small -> 0.75;
////            case Medium -> 1.00;
////            case Large -> 1.25;
////        };
////    }
//
//    private void scheduleSeatReleaseTask(Booking booking) {
//        long delay = Duration.ofMinutes(5).toMillis();
//
//        TimerTask task = new TimerTask() {
//
//            public void run() {
//                holdSeatsTemporarily(booking);
//            }
//        };
//
//        Timer timer = new Timer();
//        timer.schedule(task, delay);
//    }
//
//    public void holdSeatsTemporarily(Booking booking) {
//        Booking existingBooking = bookingRepository.findById(booking.getId()).orElse(null);
//
//        if (existingBooking != null && existingBooking.getPaymentMethod() == null) {
//            if (existingBooking.getStatus() == BookingStatus.In_Processing) {
//                List<Seat> heldSeats = existingBooking.getSeatList().stream()
//                        .map(BookingSeat::getSeat)
//                        .toList();
//                heldSeats.forEach(seat -> seat.setSeatStatus(SeatStatus.Available));
//                seatRepository.saveAll(heldSeats);
//
//                bookingRepository.delete(existingBooking);
//
//                System.out.printf("Seats have been released for booking ID: %d due to incomplete payment within 5 minutes.%n", booking.getId());
//
//                Notification notification = new Notification();
//                notification.setUser(existingBooking.getUser());
//                notification.setMessage("Your booking has been canceled because booking was not completed within 5 minutes!");
//                notification.setDateCreated(LocalDateTime.now());
//                notificationRepository.save(notification);
//            }
//        }
//    }
}

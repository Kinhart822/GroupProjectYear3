package com.spring.service.impl;

import com.spring.dto.request.booking.*;
import com.spring.dto.response.booking.BookingResponse;
import com.spring.dto.response.booking.*;
import com.spring.entities.*;
import com.spring.enums.*;
import com.spring.repository.*;
import com.spring.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieGenreRepository movieGenreRepository;

    @Autowired
    private MoviePerformerRepository moviePerformerRepository;

    @Autowired
    private MovieRatingDetailRepository movieRatingDetailRepository;

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MovieScheduleRepository movieScheduleRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private DrinkRepository drinkRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private BookingTicketRepository bookingTicketRepository;

    @Autowired
    private BookingSeatRepository bookingSeatRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public List<MovieResponse> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        List<MovieResponse> bookingMovieResponses = new ArrayList<>();

        for (Movie movie : movies) {
            // Fetch Movie Genres
            List<MovieGenre> movieGenres = movieGenreRepository.findMovieGenresByMovie(movie.getId());
            List<String> movieGenreNameList = movieGenres.stream()
                    .map(movieGenre -> movieGenre.getMovieGenreDetail().getName())
                    .toList();
            List<String> movieGenreImageUrls = movieGenres.stream()
                    .map(movieGenre -> movieGenre.getMovieGenreDetail().getImageUrl())
                    .toList();
            List<String> movieGenreDescriptions = movieGenres.stream()
                    .map(movieGenre -> movieGenre.getMovieGenreDetail().getDescription())
                    .toList();

            // Fetch Movie Performers
            List<MoviePerformer> moviePerformers = moviePerformerRepository.findMoviePerformersByMovieId(movie.getId());
            List<String> moviePerformerNameList = moviePerformers.stream()
                    .map(moviePerformer -> moviePerformer.getMoviePerformerDetail().getName())
                    .toList();

            SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
            List<Date> moviePerformerDobList = moviePerformers.stream()
                    .map(moviePerformer -> moviePerformer.getMoviePerformerDetail().getDob())
                    .toList();
            List<String> formatMoviePerformerDobList = new ArrayList<>();
            for (Date dob : moviePerformerDobList) {
                formatMoviePerformerDobList.add(formatterDate.format(dob));
            }

            List<PerformerSex> moviePerformerSex = moviePerformers.stream()
                    .map(moviePerformer -> moviePerformer.getMoviePerformerDetail().getPerformerSex())
                    .toList();
            List<PerformerType> moviePerformerType = moviePerformers.stream()
                    .map(moviePerformer -> moviePerformer.getMoviePerformerDetail().getPerformerType())
                    .toList();

            // Fetch Movie Rating Details
            List<MovieRatingDetail> movieRatingDetails = movieRatingDetailRepository.findMovieRatingDetailsByMovieId(movie.getId());
            List<String> movieRatingDetailNameList = movieRatingDetails.stream()
                    .map(MovieRatingDetail::getName)
                    .toList();
            List<String> movieRatingDetailDescriptions = movieRatingDetails.stream()
                    .map(MovieRatingDetail::getDescription)
                    .toList();

            // Format date
            SimpleDateFormat publishDateFormatter = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDatePublish = publishDateFormatter.format(movie.getDatePublish());

            // Movie Respond
            List<Comment> comments = commentRepository.findByMovieId(movie.getId());
            List<String> contents = comments.stream()
                    .map(Comment::getContent)
                    .toList();

            List<Rating> ratings = ratingRepository.findByMovieId(movie.getId());
            OptionalDouble averageRating = ratings.stream()
                    .mapToDouble(Rating::getRatingStar)
                    .average();

            double avg = 0;
            if (averageRating.isPresent()) {
                avg = averageRating.getAsDouble();
                System.out.printf("Average rating: %s%n", avg);
            } else {
                System.out.println("No ratings available.");
            }

            // Create and add MovieResponse object
            MovieResponse movieResponse = new MovieResponse(
                    movie.getId(),
                    movie.getName(),
                    movie.getLength(),
                    formattedDatePublish,
                    movie.getTrailerLink(),
                    movie.getImageUrl(),
                    movieGenreNameList,
                    movieGenreImageUrls,
                    movieGenreDescriptions,
                    moviePerformerNameList,
                    formatMoviePerformerDobList,
                    moviePerformerSex,
                    moviePerformerType,
                    movieRatingDetailNameList,
                    movieRatingDetailDescriptions,
                    contents,
                    avg
            );
            bookingMovieResponses.add(movieResponse);
        }

        return bookingMovieResponses;
    }

    @Override
    public List<CityResponse> getAllCitiesBySelectedMovie(Integer movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie"));

        List<City> cities = cityRepository.findByMovieId(movie.getId());
        if (cities == null || cities.isEmpty()) {
            throw new IllegalArgumentException("No cities found for given movie.");
        }

        List<CityResponse> cityResponses = new ArrayList<>();
        for (City city : cities) {
            List<Cinema> cinemaList = city.getCinemaList();
            List<String> cinemaNameList = cinemaList.stream()
                    .map(Cinema::getName)
                    .toList();

            CityResponse cityResponse = new CityResponse(
                    movie.getName(),
                    city.getId(),
                    city.getName(),
                    cinemaNameList
            );
            cityResponses.add(cityResponse);
        }

        return cityResponses;
    }

    @Override
    public List<CinemaResponse> getAllCinemasBySelectedCity(Integer cityId) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid city"));

        List<Cinema> cinemaList = cinemaRepository.findByCityId(city.getId());
        if (cinemaList == null || cinemaList.isEmpty()) {
            throw new IllegalArgumentException("No cinemas found for given city.");
        }

        List<CinemaResponse> cinemaResponses = new ArrayList<>();
        for (Cinema cinema : cinemaList) {
            List<Screen> screenList = cinema.getScreenList();
            List<String> screenType = screenList.stream()
                    .map(screen -> screen.getScreenType().getName())
                    .toList();
            List<String> screenDescription = screenList.stream()
                    .map(screen -> screen.getScreenType().getDescription())
                    .toList();

            List<Food> foodList = cinema.getFoodList();
            List<String> foodName = foodList.stream().map(Food::getName).toList();

            List<Drink> drinks = cinema.getDrinks();
            List<String> drinkName = drinks.stream().map(Drink::getName).toList();

            List<MovieSchedule> movieSchedules = movieScheduleRepository.findMovieSchedulesByCinemaId(cinema.getId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
            List<String> formattedSchedules = movieSchedules.stream()
                    .map(schedule -> schedule.getStartTime().format(formatter))
                    .toList();

            CinemaResponse cinemaResponse = new CinemaResponse(
                    city.getName(),
                    cinema.getId(),
                    cinema.getName(),
                    screenType,
                    screenDescription,
                    foodName,
                    drinkName,
                    formattedSchedules
            );
            cinemaResponses.add(cinemaResponse);
        }

        return cinemaResponses;
    }

    @Override
    public List<ScreenResponse> getAllScreensBySelectedCinema(Integer cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cinema"));

        List<Screen> screenList = screenRepository.findByCinemaId(cinema.getId());
        if (screenList == null || screenList.isEmpty()) {
            throw new IllegalArgumentException("No screens found for given cinema.");
        }
        List<ScreenResponse> screenResponses = new ArrayList<>();

        for (Screen screen : screenList) {
            ScreenResponse screenResponse = new ScreenResponse(
                    cinema.getName(),
                    screen.getId(),
                    screen.getName(),
                    screen.getScreenType().getName(),
                    screen.getScreenType().getDescription()
            );
            screenResponses.add(screenResponse);
        }

        return screenResponses;
    }

    @Override
    public List<ScheduleResponse> getAllSchedulesBySelectedMovieAndSelectedCinemaAndSelectedScreen(
            Integer movieId, Integer cinemaId, Integer screenId
    ) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie"));

        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cinema"));

        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid screen"));

        List<MovieSchedule> movieSchedules = movieScheduleRepository.findMovieSchedulesByMovieAndCinemaAndScreen(
                movie.getId(), cinema.getId(), screen.getId()
        );
        if (movieSchedules == null || movieSchedules.isEmpty()) {
            throw new IllegalArgumentException("No schedules found for given movie, cinema, and screen.");
        }

        List<ScheduleResponse> scheduleResponses = new ArrayList<>();
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");

        for (MovieSchedule schedule : movieSchedules) {
            ScheduleResponse scheduleResponse = new ScheduleResponse(
                    movie.getName(),
                    cinema.getName(),
                    screen.getName(),
                    schedule.getId(),
                    schedule.getStartTime().format(formatterDate),
                    schedule.getStartTime().format(formatterTime)
            );
            scheduleResponses.add(scheduleResponse);
        }

        return scheduleResponses;
    }

    @Override
    public List<TicketResponse> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        List<TicketResponse> ticketResponses = new ArrayList<>();

        for (Ticket ticket : tickets) {
            List<Integer> ticketsIds = new ArrayList<>();
            List<String> ticketTypes = new ArrayList<>();
            List<String> ticketDescriptions = new ArrayList<>();

            ticketsIds.add(ticket.getId());
            ticketTypes.add(ticket.getTicketType().getName());
            ticketDescriptions.add(ticket.getTicketType().getDescription());

            TicketResponse response = new TicketResponse(ticketsIds, ticketTypes, ticketDescriptions);
            ticketResponses.add(response);
        }

        return ticketResponses;
    }

    @Override
    public List<SeatResponse> getAllSeatsBySelectedScreen(Integer screenId) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid screen"));

        List<Seat> availableSeats = seatRepository.findBySeatStatusAndScreenId(SeatStatus.Available, screen.getId());
        if (availableSeats == null || availableSeats.isEmpty()) {
            throw new IllegalArgumentException("No available seats found for given screen.");
        }

        List<Seat> unAvailableSeats = seatRepository.findBySeatStatusAndScreenId(SeatStatus.Unavailable, screen.getId());

        List<Seat> heldSeats = seatRepository.findBySeatStatusAndScreenId(SeatStatus.Held, screen.getId());

        List<String> unAvailableSeatNames = new ArrayList<>();
        List<String> unAvailableSeatsTypeList = new ArrayList<>();
        List<String> heldSeatNames = new ArrayList<>();
        List<String> heldSeatsTypeList = new ArrayList<>();
        List<SeatResponse> seatResponses = new ArrayList<>();

        for (Seat seat : unAvailableSeats) {
            unAvailableSeatNames.add(seat.getName());
            unAvailableSeatsTypeList.add(seat.getSeatType().getName());
        }

        for (Seat seat : heldSeats) {
            heldSeatNames.add(seat.getName());
            heldSeatsTypeList.add(seat.getSeatType().getName());
        }

        for (Seat seat : availableSeats) {
            SeatResponse seatResponse = new SeatResponse(
                    screen.getName(),
                    unAvailableSeatNames,
                    unAvailableSeatsTypeList,
                    seat.getId(),
                    seat.getName(),
                    seat.getColumn(),
                    seat.getRow(),
                    seat.getSeatType().getName(),
                    heldSeatNames,
                    heldSeatsTypeList
            );
            seatResponses.add(seatResponse);
        }

        return seatResponses;
    }

    @Override
    public List<ListFoodAndDrinkToOrderingResponse> getAllFoodsAndDrinksByCinema(Integer cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cinema"));

        List<Food> foods = foodRepository.findByCinema(cinema);
        if (foods == null || foods.isEmpty()) {
            throw new IllegalArgumentException("No foods found for given cinema.");
        }
        List<Drink> drinks = drinkRepository.findByCinema(cinema);
        if (drinks == null || drinks.isEmpty()) {
            throw new IllegalArgumentException("No drinks found for given cinema.");
        }

        List<Integer> foodIds = new ArrayList<>();
        List<String> foodNameList = new ArrayList<>();
        List<String> imageUrlFoodList = new ArrayList<>();
        List<String> descriptionFoodList = new ArrayList<>();
        List<SizeFoodOrDrink> sizeFoodList = new ArrayList<>();

        List<Integer> drinkIds = new ArrayList<>();
        List<String> drinkNameList = new ArrayList<>();
        List<String> imageUrlDrinkList = new ArrayList<>();
        List<String> descriptionDrinkList = new ArrayList<>();
        List<SizeFoodOrDrink> sizeDrinkList = new ArrayList<>();

        for (Food food : foods) {
            foodIds.add(food.getId());
            foodNameList.add(food.getName());
            imageUrlFoodList.add(food.getImageUrl());
            descriptionFoodList.add(food.getDescription());
            sizeFoodList.add(food.getSize());
        }

        for (Drink drink : drinks) {
            drinkIds.add(drink.getId());
            drinkNameList.add(drink.getName());
            imageUrlDrinkList.add(drink.getImageUrl());
            descriptionDrinkList.add(drink.getDescription());
            sizeDrinkList.add(drink.getSize());
        }

        ListFoodAndDrinkToOrderingResponse listFoodAndDrinkToOrderingResponse = new ListFoodAndDrinkToOrderingResponse(
                cinema.getName(),
                foodIds,
                foodNameList,
                imageUrlFoodList,
                descriptionFoodList,
                sizeFoodList,
                drinkIds,
                drinkNameList,
                imageUrlDrinkList,
                descriptionDrinkList,
                sizeDrinkList
        );

        return List.of(listFoodAndDrinkToOrderingResponse);
    }

    @Override
    public List<CouponResponse> getAllCouponsByUser(Integer userId) {
        List<Coupon> availableUserCouponIds = couponRepository.findAvailableCouponsForUser(userId);
        if (availableUserCouponIds == null || availableUserCouponIds.isEmpty()) {
            throw new IllegalArgumentException("No available coupons found for given user.");
        }

        List<Integer> couponIds = new ArrayList<>();
        List<String> couponNameList = new ArrayList<>();
        List<String> couponDescriptionList = new ArrayList<>();
        List<BigDecimal> discountRateList = new ArrayList<>();
        List<Integer> minSpendReqList = new ArrayList<>();
        List<Integer> discountLimitList = new ArrayList<>();

        for (Coupon coupon : availableUserCouponIds) {
            couponIds.add(coupon.getId());
            couponNameList.add(coupon.getName());
            couponDescriptionList.add(coupon.getDescription());
            discountRateList.add(coupon.getDiscount());
            minSpendReqList.add(coupon.getMinSpendReq());
            discountLimitList.add(coupon.getDiscountLimit());
        }

        CouponResponse couponResponse = new CouponResponse(
                couponIds,
                couponNameList,
                couponDescriptionList,
                discountRateList,
                minSpendReqList,
                discountLimitList
        );

        return List.of(couponResponse);
    }

    @Override
    public List<CouponResponse> getAllCouponsByMovie(Integer movieId) {
        List<Coupon> availableMovieCouponIds = couponRepository.findAvailableCouponsByMovieId(movieId);
        if (availableMovieCouponIds == null || availableMovieCouponIds.isEmpty()) {
            throw new IllegalArgumentException("No available coupons found for given movie.");
        }

        List<Integer> couponIds = new ArrayList<>();
        List<String> couponNameList = new ArrayList<>();
        List<String> couponDescriptionList = new ArrayList<>();
        List<BigDecimal> discountRateList = new ArrayList<>();
        List<Integer> minSpendReqList = new ArrayList<>();
        List<Integer> discountLimitList = new ArrayList<>();

        for (Coupon coupon : availableMovieCouponIds) {
            couponIds.add(coupon.getId());
            couponNameList.add(coupon.getName());
            couponDescriptionList.add(coupon.getDescription());
            discountRateList.add(coupon.getDiscount());
            minSpendReqList.add(coupon.getMinSpendReq());
            discountLimitList.add(coupon.getDiscountLimit());
        }

        CouponResponse couponResponse = new CouponResponse(
                couponIds,
                couponNameList,
                couponDescriptionList,
                discountRateList,
                minSpendReqList,
                discountLimitList
        );

        return List.of(couponResponse);
    }

    @Override
    public SendBookingResponse processingBooking(Integer userId, BookingRequest bookingRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user"));

        Booking booking = new Booking();
        booking.setUser(user);

        LocalDate now = LocalDate.now();
        LocalDate maxAllowedDate = now.plusDays(4);

        Movie movie = movieRepository.findById(bookingRequest.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie"));

        LocalDate datePublish = movie.getDatePublish().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (datePublish.isAfter(maxAllowedDate)) {
            throw new IllegalArgumentException("The movie cannot be selected because it have a schedule set more than a 4 days into the future.");
        }
        booking.setMovie(movie);

        City city = cityRepository.findById(bookingRequest.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid city"));
        if (city.getMovie().getId() != movie.getId()) {
            throw new IllegalArgumentException("The selected city does not match the specified movie!");
        }
        booking.setCity(city);

        Cinema cinema = cinemaRepository.findById(bookingRequest.getCinemaId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid cinema"));
        if (cinema.getCity().getId() != city.getId()) {
            throw new IllegalArgumentException("The selected cinema does not match the specified city!");
        }
        booking.setCinema(cinema);

        Screen screen = screenRepository.findById(bookingRequest.getScreenId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid screen"));
        if (screen.getCinema().getId() != cinema.getId()) {
            throw new IllegalArgumentException("The selected screen does not match the specified cinema!");
        }
        booking.setScreen(screen);

        MovieSchedule selectedSchedule = movieScheduleRepository.findById(bookingRequest.getScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie schedule"));
        if (selectedSchedule.getMovie().getId() != movie.getId() && selectedSchedule.getCinema().getId() != cinema.getId()
                && selectedSchedule.getScreen().getId() != screen.getId()) {
            throw new IllegalArgumentException("The selected schedule does not match the specified cinema!");
        }

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime movieStartTime = selectedSchedule.getStartTime();
        LocalDateTime movieStartTimePlus10Minutes = movieStartTime.plusMinutes(10);

        if (currentTime.isAfter(movieStartTime) && currentTime.isAfter(movieStartTimePlus10Minutes)) {
            throw new IllegalArgumentException("Cannot book a ticket after the movie has started for more than 10 minutes.");
        }

        LocalDateTime startDateTime = selectedSchedule.getStartTime();
        int lengthInMinutes = movie.getLength();
        long lengthInSeconds = lengthInMinutes * 60L;
        Duration movieDuration = Duration.ofSeconds(lengthInSeconds);
        LocalDateTime endDateTime = startDateTime.plus(movieDuration);

        booking.setStartDateTime(startDateTime);
        booking.setEndDateTime(endDateTime);
        booking.setMovieSchedule(selectedSchedule);

        if (bookingRequest.getTicketIds() != null && !bookingRequest.getTicketIds().isEmpty()) {
            Map<Integer, BookingTicket> ticketMap = new HashMap<>();

            for (Integer ticketId : bookingRequest.getTicketIds()) {
                Ticket ticket = ticketRepository.findById(ticketId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid ticket ID"));

                BookingTicket bookingTicket = ticketMap.computeIfAbsent(ticketId, id -> {
                    BookingTicket newBookingTicket = new BookingTicket();
                    newBookingTicket.setBooking(booking);
                    newBookingTicket.setTicket(ticket);
                    newBookingTicket.setQuantity(0);
                    return newBookingTicket;
                });

                bookingTicket.setQuantity(bookingTicket.getQuantity() + 1);
            }
            booking.setTickets(new ArrayList<>(ticketMap.values()));
        }

        double totalPrice = booking.getTickets().stream()
                .mapToDouble(bookingTicket -> bookingTicket.getTicket().getTicketType().getPrice() * bookingTicket.getQuantity())
                .sum();
        booking.setTotalPrice(totalPrice);

        int ticketCount = booking.getTickets().stream()
                .mapToInt(BookingTicket::getQuantity)
                .sum();
        int seatCount = bookingRequest.getSeatIds().size();
        if (seatCount != ticketCount) {
            throw new IllegalArgumentException("The number of selected seats (%d) does not match the number of tickets (%d).".formatted(seatCount, ticketCount));
        }

        Set<Integer> seatIdSet = new HashSet<>(bookingRequest.getSeatIds());
        if (seatIdSet.size() != bookingRequest.getSeatIds().size()) {
            throw new IllegalArgumentException("Duplicate seat IDs detected in the request.");
        }

        List<Seat> selectedSeats = seatRepository.findAllById(bookingRequest.getSeatIds())
                .stream()
                .toList();
        if (selectedSeats.size() != seatCount) {
            throw new IllegalArgumentException("One or more selected seats are unavailable.");
        }
        if (selectedSeats.stream().anyMatch(seat -> seat.getScreen().getId() != screen.getId())) {
            throw new IllegalArgumentException("The selected seats do not match the specified screen!");
        }
        if (selectedSeats.stream().anyMatch(seat -> seat.getSeatStatus() == SeatStatus.Unavailable)) {
            throw new IllegalArgumentException("Selected seats are already unavailable, please select a different seat.");
        }
        if (selectedSeats.stream().anyMatch(seat -> seat.getSeatStatus() == SeatStatus.Held)) {
            throw new IllegalArgumentException("Selected seats are already held, please select a different seat.");
        }

        if (bookingRequest.getSeatIds() != null && !bookingRequest.getSeatIds().isEmpty()) {
            Map<Integer, BookingSeat> seatMap = new HashMap<>();

            for (Seat seat : selectedSeats) {
                BookingSeat bookingSeat = seatMap.computeIfAbsent(seat.getId(), id -> {
                    BookingSeat newBookingSeat = new BookingSeat();
                    newBookingSeat.setBooking(booking);
                    newBookingSeat.setSeat(seat);
                    newBookingSeat.setSeatType(seat.getSeatType());
                    return newBookingSeat;
                });
                seat.setSeatStatus(SeatStatus.Held);
            }
            booking.setSeatList(new ArrayList<>(seatMap.values()));
        }

        double totalPriceSeat = booking.getSeatList().stream()
                .mapToDouble(draftSeat -> draftSeat.getSeatType().getPrice())
                .sum();
        double updateTotalPrice = booking.getTotalPrice() + totalPriceSeat;
        booking.setTotalPrice(updateTotalPrice);

        if (bookingRequest.getFoodIds() != null && !bookingRequest.getFoodIds().isEmpty()) {
            Map<String, BookingFood> foodMap = new HashMap<>();
            double totalFoodPrice = 0.0;

            for (int i = 0; i < bookingRequest.getFoodIds().size(); i++) {
                Integer foodId = bookingRequest.getFoodIds().get(i);
                SizeFoodOrDrink size = bookingRequest.getSizeFoodList().get(i);

                Food food = foodRepository.findById(foodId)
                        .orElseThrow(() -> new IllegalArgumentException("Food not found"));
                if (food.getCinema().getId() != cinema.getId()) {
                    throw new IllegalArgumentException("The selected food does not match the specified cinema!");
                }

                String key = "%d-%s".formatted(foodId, size.name());

                BookingFood bookingFood = foodMap.computeIfAbsent(key, k -> {
                    BookingFood newBookingFood = new BookingFood();
                    newBookingFood.setBooking(booking);
                    newBookingFood.setFood(food);
                    newBookingFood.setSizeFood(size);
                    newBookingFood.setQuantity(1);
                    return newBookingFood;
                });

                bookingFood.setQuantity(bookingFood.getQuantity() + 1);
                double price = food.getPrice() * getFoodOrDrinkSize(size) * bookingFood.getQuantity();
                totalFoodPrice += price;
            }

            updateTotalPrice = booking.getTotalPrice() + totalFoodPrice;
            booking.setTotalPrice(updateTotalPrice);

            booking.setFoodList(new ArrayList<>(foodMap.values()));
        }

        if (bookingRequest.getDrinkIds() != null && !bookingRequest.getDrinkIds().isEmpty()) {
            Map<String, BookingDrink> drinkMap = new HashMap<>();
            double totalDrinkPrice = 0.0;

            for (int i = 0; i < bookingRequest.getDrinkIds().size(); i++) {
                Integer drinkId = bookingRequest.getDrinkIds().get(i);
                SizeFoodOrDrink size = bookingRequest.getSizeDrinkList().get(i);

                Drink drink = drinkRepository.findById(drinkId)
                        .orElseThrow(() -> new IllegalArgumentException("Drink not found"));
                if (drink.getCinema().getId() != cinema.getId()) {
                    throw new IllegalArgumentException("The selected drink does not match the specified cinema!");
                }

                String key = "%d-%s".formatted(drinkId, size.name());

                BookingDrink bookingDrink = drinkMap.computeIfAbsent(key, k -> {
                    BookingDrink newBookingDrink = new BookingDrink();
                    newBookingDrink.setBooking(booking);
                    newBookingDrink.setDrink(drink);
                    newBookingDrink.setSizeDrink(size);
                    newBookingDrink.setQuantity(1);
                    return newBookingDrink;
                });

                bookingDrink.setQuantity(bookingDrink.getQuantity() + 1);
                double price = drink.getPrice() * getFoodOrDrinkSize(size) * bookingDrink.getQuantity();
                totalDrinkPrice += price;
            }

            updateTotalPrice = booking.getTotalPrice() + totalDrinkPrice;
            booking.setTotalPrice(updateTotalPrice);

            booking.setDrinks(new ArrayList<>(drinkMap.values()));
        }

        List<BookingCoupon> bookingCoupons = new ArrayList<>();
        double discountAmount;

        // Fetch and validate selected movie coupon
        if (bookingRequest.getMovieCouponId() != null) {
            Coupon selectedMovieCoupon = couponRepository.findCouponByMovieIdAndCouponId(
                    bookingRequest.getMovieId(),
                    bookingRequest.getMovieCouponId()
            );
            if (selectedMovieCoupon == null) {
                throw new IllegalArgumentException("Coupon not found for movie %d.".formatted(bookingRequest.getMovieId()));
            }
            if (selectedMovieCoupon.getDateAvailable().after(new Date()) || selectedMovieCoupon.getDateExpired().before(new Date())) {
                throw new IllegalArgumentException("Coupon %d is not valid.".formatted(selectedMovieCoupon.getId()));
            }

            if (totalPrice < selectedMovieCoupon.getMinSpendReq()) {
                throw new IllegalArgumentException("Minimum spend requirement not met for coupon %d.".formatted(selectedMovieCoupon.getId()));
            }

            discountAmount = updateTotalPrice * selectedMovieCoupon.getDiscount().doubleValue();
            discountAmount = Math.min(discountAmount, selectedMovieCoupon.getDiscountLimit());
            updateTotalPrice -= discountAmount;

            BookingCoupon bookingCoupon = new BookingCoupon();
            bookingCoupon.setBooking(booking);
            bookingCoupon.setCoupon(selectedMovieCoupon);
            bookingCoupons.add(bookingCoupon);
        }

        // Fetch and validate selected user coupon
        if (bookingRequest.getUserCouponId() != null) {
            Coupon selectedUserCoupon = couponRepository.findCouponByUserIdAndCouponId(userId, bookingRequest.getUserCouponId());
            if (selectedUserCoupon == null) {
                throw new IllegalArgumentException("Coupon not found for user %d.".formatted(userId));
            }
            if (selectedUserCoupon.getDateAvailable().after(new Date()) || selectedUserCoupon.getDateExpired().before(new Date())) {
                throw new IllegalArgumentException("Coupon %d is not valid.".formatted(selectedUserCoupon.getId()));
            }

            if (totalPrice < selectedUserCoupon.getMinSpendReq()) {
                throw new IllegalArgumentException("Minimum spend requirement not met for coupon %d.".formatted(selectedUserCoupon.getId()));
            }

            discountAmount = updateTotalPrice * selectedUserCoupon.getDiscount().doubleValue();
            discountAmount = Math.min(discountAmount, selectedUserCoupon.getDiscountLimit());
            updateTotalPrice -= discountAmount;

            BookingCoupon bookingCoupon = new BookingCoupon();
            bookingCoupon.setBooking(booking);
            bookingCoupon.setCoupon(selectedUserCoupon);
            bookingCoupons.add(bookingCoupon);
        }

        updateTotalPrice = BigDecimal.valueOf(updateTotalPrice)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        booking.setCoupons(bookingCoupons);
        booking.setTotalPrice(updateTotalPrice);
        booking.setStatus(BookingStatus.In_Processing);
        bookingRepository.save(booking);

            // Schedule seat release if payment is not completed
        scheduleSeatReleaseTask(booking);

        return new SendBookingResponse(
                booking.getId(),
                booking.getMovie().getName(),
                booking.getCity().getName(),
                booking.getCinema().getName(),
                booking.getStartDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm a")),
                booking.getEndDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm a")),
                booking.getScreen().getName(),
                booking.getTickets().stream().map(ticket -> ticket.getTicket().getTicketType().getName()).toList(),
                booking.getSeatList().stream().map(seat -> "%s (%s)".formatted(seat.getSeat().getName(), seat.getSeatType().getName())).toList(),
                booking.getFoodList().stream().map(food -> "%s (%s)".formatted(food.getFood().getName(), food.getSizeFood())).toList(),
                booking.getDrinks().stream().map(drink -> "%s (%s)".formatted(drink.getDrink().getName(), drink.getSizeDrink())).toList(),
                booking.getStatus().toString()
        );
    }

    @Override
    public BookingResponse completeBooking(Integer userId, BookingRequest bookingRequest) {
        Booking booking = bookingRepository.findById(bookingRequest.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found for ID: %d".formatted(bookingRequest.getBookingId())));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found for ID: %d".formatted(userId)));

        if (isBookingIncomplete(booking)) {
            deleteBooking(booking.getId(), userId);
            throw new IllegalArgumentException("Booking is still not completed. Please fill all the booking information again!");
        }

        if (booking.getSeatList().stream().anyMatch(seat -> seat.getSeat().getSeatStatus() == SeatStatus.Unavailable)) {
            throw new IllegalArgumentException("Some of the selected seats are already bought by another user. Please fill all the booking information again!");
        }


        if (booking.getSeatList().stream().anyMatch(seat -> seat.getSeat().getSeatStatus() != SeatStatus.Held)) {
            throw new IllegalArgumentException("Seat is still not held. Please fill all the booking information again!");
        }

        booking.setBookingNo(generateBookingNo(user.getLastName(), user.getFirstName(), booking.getMovie().getName()));
        booking.setPaymentMethod(bookingRequest.getPaymentMethod());
        booking.setStatus(bookingRequest.getPaymentMethod() == PaymentMethod.Cash
                ? BookingStatus.Pending_Payment
                : BookingStatus.Confirmed);
        if (booking.getStatus() == BookingStatus.Confirmed) {
            booking.getSeatList().forEach(seat -> seat.getSeat().setSeatStatus(SeatStatus.Unavailable));
        }

        List<Seat> seatsToUpdate = booking.getSeatList().stream()
                .map(BookingSeat::getSeat)
                .collect(Collectors.toList());
        seatRepository.saveAll(seatsToUpdate);

        bookingRepository.save(booking);

        if (booking.getPaymentMethod().equals(PaymentMethod.Bank_Transfer)) {
            booking.setStatus(BookingStatus.Completed);
        }


        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage("Your booking for %s is confirmed. Booking Number: %s".formatted(booking.getMovie().getName(), booking.getBookingNo()));
        notification.setDateCreated(LocalDateTime.now());
        notificationRepository.save(notification);

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm a");
            String formattedStartDateTime = booking.getStartDateTime().format(formatter);
            String formattedEndDateTime = booking.getEndDateTime().format(formatter);
            String subject = "Your Movie Booking Confirmation";
            String body = "Dear %s %s,\n\nYour booking for the movie \"%s\" is confirmed!\nBooking Number: %s\nDate & Time: %s - %s\nCinema: %s, %s\nScreen: %s\nTickets: %s\nSeats: %s\nFood: %s\nDrinks: %s\nTotal Price: $%s\n\nThank you for booking with us!\n\nBest regards,\nYour Movie Booking Team".formatted(user.getFirstName(), user.getLastName(), booking.getMovie().getName(), booking.getBookingNo(), formattedStartDateTime, formattedEndDateTime, booking.getCinema().getName(), booking.getCity().getName(), booking.getScreen().getName(), booking.getTickets().stream()
                    .map(ticket -> ticket.getTicket().getTicketType().getName())
                    .collect(Collectors.joining(", ")), booking.getSeatList().stream()
                    .map(seat -> "%s (%s)".formatted(seat.getSeat().getName(), seat.getSeatType().getName()))
                    .collect(Collectors.joining(", ")), booking.getFoodList().stream()
                    .map(food -> "%s (%s)".formatted(food.getFood().getName(), food.getSizeFood()))
                    .collect(Collectors.joining(", ")), booking.getDrinks().stream()
                    .map(drink -> "%s (%s)".formatted(drink.getDrink().getName(), drink.getSizeDrink()))
                    .collect(Collectors.joining(", ")), booking.getTotalPrice());
            emailService.sendSimpleMailMessage(user.getEmail(), subject, body);
        } catch (Exception e) {
            System.out.println("Failed to send email notification. Please try again later.");
        }

        return new BookingResponse(
                booking.getBookingNo(),
                booking.getMovie().getName(),
                booking.getCity().getName(),
                booking.getCinema().getName(),
                booking.getStartDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm a")),
                booking.getEndDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm a")),
                booking.getScreen().getName(),
                booking.getTickets().stream().map(ticket -> ticket.getTicket().getTicketType().getName()).toList(),
                booking.getSeatList().stream().map(seat -> "%s (%s)".formatted(seat.getSeat().getName(), seat.getSeatType().getName())).toList(),
                booking.getFoodList().stream().map(food -> "%s (%s)".formatted(food.getFood().getName(), food.getSizeFood())).toList(),
                booking.getDrinks().stream().map(drink -> "%s (%s)".formatted(drink.getDrink().getName(), drink.getSizeDrink())).toList(),
                booking.getTotalPrice(),
                booking.getStatus().toString()
        );
    }

    @Override
    public void updateBookingSeat(BookingRequest bookingRequest) {
        Booking booking = bookingRepository.findById(bookingRequest.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found for ID: %d".formatted(bookingRequest.getBookingId())));
        if (bookingRequest.getSeatIds() == null || bookingRequest.getSeatIds().isEmpty()) return;

        double oldTotalPrice = booking.getTotalPrice();
        double oldTotalPriceSeat = booking.getSeatList().stream()
                .mapToDouble(draftSeat -> draftSeat.getSeatType().getPrice())
                .sum();

        for (BookingSeat bookingSeat : booking.getSeatList()) {
            bookingSeat.setSeat(null);
            bookingSeatRepository.delete(bookingSeat);
        }

        int ticketCount = booking.getTickets().stream()
                .mapToInt(BookingTicket::getQuantity)
                .sum();
        int seatCount = bookingRequest.getSeatIds().size();
        if (seatCount != ticketCount) {
            throw new IllegalArgumentException("The number of selected seats (%d) does not match the number of tickets (%d).".formatted(seatCount, ticketCount));
        }

        Set<Integer> seatIdSet = new HashSet<>(bookingRequest.getSeatIds());
        if (seatIdSet.size() != bookingRequest.getSeatIds().size()) {
            throw new IllegalArgumentException("Duplicate seat IDs detected in the request.");
        }

        List<Seat> selectedSeats = seatRepository.findAllById(bookingRequest.getSeatIds())
                .stream()
                .toList();
        if (selectedSeats.size() != seatCount) {
            throw new IllegalArgumentException("One or more selected seats are unavailable.");
        }
        if (selectedSeats.stream().anyMatch(seat -> seat.getScreen().getId() != booking.getScreen().getId())) {
            throw new IllegalArgumentException("The selected seats do not match the specified screen!");
        }
        if (selectedSeats.stream().anyMatch(seat -> seat.getSeatStatus() == SeatStatus.Unavailable)) {
            throw new IllegalArgumentException("Selected seats are already unavailable, please select a different seat.");
        }

        if (bookingRequest.getSeatIds() != null && !bookingRequest.getSeatIds().isEmpty()) {
            Map<Integer, BookingSeat> seatMap = new HashMap<>();

            for (Seat seat : selectedSeats) {
                BookingSeat bookingSeat = seatMap.computeIfAbsent(seat.getId(), id -> {
                    BookingSeat newBookingSeat = new BookingSeat();
                    newBookingSeat.setBooking(booking);
                    newBookingSeat.setSeat(seat);
                    newBookingSeat.setSeatType(seat.getSeatType());
                    return newBookingSeat;
                });
                seat.setSeatStatus(SeatStatus.Held);
            }
            booking.setSeatList(new ArrayList<>(seatMap.values()));
        }
        double newTotalSeatPrice = booking.getSeatList().stream()
                .mapToDouble(draftSeat -> draftSeat.getSeatType().getPrice())
                .sum();

        double updatedTotalPrice = oldTotalPrice - oldTotalPriceSeat + newTotalSeatPrice;
        booking.setTotalPrice(updatedTotalPrice);
    }

    @Override
    public void cancelBooking(Integer bookingId, Integer userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found for ID: %d".formatted(bookingId)));

        if (booking.getStartDateTime().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new IllegalArgumentException("You cannot cancel a booking within 1 hour of the start time.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        List<Seat> seatListChangeToAvailable = new ArrayList<>();
        for (BookingSeat seat : booking.getSeatList()) {
            seat.getSeat().setSeatStatus(SeatStatus.Available);
            seatListChangeToAvailable.add(seat.getSeat());
        }
        seatRepository.saveAll(seatListChangeToAvailable);

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        try {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setMessage("Your booking for %s is canceled. Booking Number: %s".formatted(booking.getMovie().getName(), booking.getBookingNo()));
            notification.setDateCreated(LocalDateTime.now());
            notificationRepository.save(notification);

            emailService.sendCancelMailMessage(user.getEmail());
            System.out.printf("Notification sent to %s%n", user.getEmail());
        } catch (Exception e) {
            System.out.println("Failed to send email notification. Please try again later.");
        }

    }

    @Override
    public void deleteBooking(Integer bookingId, Integer userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found for ID: %d".formatted(bookingId)));

        if (booking.getStartDateTime().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new IllegalArgumentException("You cannot delete a booking within 1 hour of the start time.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        List<Seat> seatListChangeToAvailable = new ArrayList<>();
        for (BookingSeat seat : booking.getSeatList()) {
            seat.getSeat().setSeatStatus(SeatStatus.Available);
            seatListChangeToAvailable.add(seat.getSeat());
        }
        seatRepository.saveAll(seatListChangeToAvailable);

        for (BookingTicket bookingTicket : booking.getTickets()) {
            bookingTicket.setTicket(null);
            bookingTicketRepository.delete(bookingTicket);
        }

        bookingRepository.delete(booking);

        try {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setMessage("Your booking for %s is deleted. Booking Number: %s".formatted(booking.getMovie().getName(), booking.getBookingNo()));
            notification.setDateCreated(LocalDateTime.now());
            notificationRepository.save(notification);

            emailService.sendDeleteMailMessage(user.getEmail());
        } catch (Exception e) {
            System.out.println("Failed to send email notification. Please try again later.");
        }
    }

    // TODO: Additional methods
    private boolean isBookingIncomplete(Booking booking) {
        return booking.getMovie() == null || booking.getCity() == null || booking.getCinema() == null ||
                booking.getScreen() == null || booking.getMovieSchedule() == null || booking.getTickets().isEmpty() ||
                booking.getSeatList().isEmpty();
    }

    private String generateBookingNo(String lastName, String firstName, String movieName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String datePart = LocalDate.now().format(formatter);
        String camelCaseMovieName = convertToCamelCase(movieName);
        String randomSuffix = String.valueOf(System.currentTimeMillis());

        return "%s%s-%s-%s-%s".formatted(firstName, lastName, camelCaseMovieName, datePart, randomSuffix);
    }

    private String convertToCamelCase(String name) {
        String[] words = name.split(" ");
        StringBuilder camelCaseName = new StringBuilder();

        if (words.length > 0) {
            camelCaseName.append(words[0].toLowerCase());
        }

        for (int i = 1; i < words.length; i++) {
            if (!words[i].isEmpty()) {
                camelCaseName.append(Character.toUpperCase(words[i].charAt(0)));
                camelCaseName.append(words[i].substring(1).toLowerCase());
            }
        }

        return camelCaseName.toString();
    }

    private double getFoodOrDrinkSize(SizeFoodOrDrink sizeFoodOrDrink) {
        return switch (sizeFoodOrDrink) {
            case Small -> 0.75;
            case Medium -> 1.00;
            case Large -> 1.25;
        };
    }

        // TODO: Đặt thời gian giữ ghế tạm thời (ví dụ: 5–10 phút). Nếu người dùng không hoàn thành thanh toán trong
        //              khoảng thời gian này, ghế sẽ được chuyển lại thành trạng thái "Available".
    private void scheduleSeatReleaseTask(Booking booking) {
        long delay = Duration.ofMinutes(5).toMillis();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                holdSeatsTemporarily(booking);
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, delay);
    }

    public void holdSeatsTemporarily(Booking booking) {
        Booking existingBooking = bookingRepository.findById(booking.getId()).orElse(null);

        if (existingBooking != null && existingBooking.getPaymentMethod() == null) {
            if (existingBooking.getStatus() == BookingStatus.In_Processing) {
                List<Seat> heldSeats = existingBooking.getSeatList().stream()
                        .map(BookingSeat::getSeat)
                        .toList();
                heldSeats.forEach(seat -> seat.setSeatStatus(SeatStatus.Available));
                seatRepository.saveAll(heldSeats);

                bookingRepository.delete(existingBooking);

                System.out.printf("Seats have been released for booking ID: %d due to incomplete payment within 5 minutes.%n", booking.getId());

                Notification notification = new Notification();
                notification.setUser(existingBooking.getUser());
                notification.setMessage("Your booking has been canceled because booking was not completed within 5 minutes!");
                notification.setDateCreated(LocalDateTime.now());
                notificationRepository.save(notification);
            }
        }
    }
}

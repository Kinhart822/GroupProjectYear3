package vn.edu.usth.mcma.backend.service;

import constants.ApiResponseCode;
import constants.CommonStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.usth.mcma.backend.dto.admin.dto.cinema.FilterCinema;
import vn.edu.usth.mcma.backend.dto.admin.dto.cinema.FilterScreen;
import vn.edu.usth.mcma.backend.dto.cinema.CinemaDetailShort;
import vn.edu.usth.mcma.backend.dto.cinema.CinemaProjection;
import vn.edu.usth.mcma.backend.exception.ApiResponse;
import vn.edu.usth.mcma.backend.exception.BusinessException;
import vn.edu.usth.mcma.backend.repository.CityRepository;
import vn.edu.usth.mcma.backend.repository.ScreenRepository;
import vn.edu.usth.mcma.backend.security.JwtHelper;
import vn.edu.usth.mcma.backend.dto.cinema.CinemaRequest;
import vn.edu.usth.mcma.backend.repository.CinemaRepository;
import vn.edu.usth.mcma.backend.entity.Cinema;

import java.time.Instant;
import java.util.List;

@Transactional
@Service
@AllArgsConstructor
public class CinemaService {
    private final CinemaRepository cinemaRepository;
    private final CityRepository cityRepository;
    private final JwtHelper jwtHelper;
    private final ScreenRepository screenRepository;

    /*
     * ADMIN
     */
    public ApiResponse createCinema(CinemaRequest request) {
        Long userId = jwtHelper.getIdUserRequesting();
        Instant now = Instant.now();
        cinemaRepository
                .save(Cinema
                        .builder()
                        .city(cityRepository
                                .findById(request.getCityId())
                                .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND)))
                        .name(request.getName())
                        .address(request.getAddress())
                        .status(CommonStatus.ACTIVE.getStatus())
                        .createdBy(userId)
                        .createdDate(now)
                        .lastModifiedBy(userId)
                        .lastModifiedDate(now)
                        .build());
        return ApiResponse.ok();
    }
    public List<CinemaProjection> findAll(String query) {
        return cinemaRepository.findAllProjectionByQuery(query);
    }
    public Cinema findById(Long id) {
        return cinemaRepository.findById(id).orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND));
    }
    public ApiResponse updateCinema(Long id, CinemaRequest request) {
        Cinema cinema = cinemaRepository.findById(id).orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND));
        cinemaRepository
                .save(cinema
                        .toBuilder()
                        .name(request.getName())
                        .address(request.getAddress())
                        .status(request.getStatus())
                        .lastModifiedBy(jwtHelper.getIdUserRequesting())
                        .lastModifiedDate(Instant.now())
                        .build());
        return ApiResponse.ok();
    }
    public ApiResponse toggleStatus(Long id) {
        Cinema cinema = cinemaRepository.findById(id).orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND));
        cinemaRepository
                .save(cinema
                        .toBuilder()
                        .status(CommonStatus.ACTIVE.getStatus() + CommonStatus.INACTIVE.getStatus() - cinema.getStatus())
                        .lastModifiedBy(jwtHelper.getIdUserRequesting())
                        .lastModifiedDate(Instant.now())
                        .build());
        return ApiResponse.ok();
    }
    public ApiResponse deactivateCinemas(List<Long> ids) {
        Long userId = jwtHelper.getIdUserRequesting();
        Instant now = Instant.now();
        cinemaRepository
                .saveAll(cinemaRepository
                        .findAllById(ids)
                        .stream()
                        .map(c -> c
                                .toBuilder()
                                .status(CommonStatus.INACTIVE.getStatus())
                                .lastModifiedBy(userId)
                                .lastModifiedDate(now)
                                .build())
                        .toList());
        return ApiResponse.ok();
    }

    /*
     * USER
     */
    public List<CinemaDetailShort> findAllCinemaByCity(Long cityId) {
        return cinemaRepository
                .findAllByCity(cityRepository
                        .findById(cityId)
                        .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND)))
                .stream()
                .map(c -> CinemaDetailShort.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .address(c.getAddress())
                        .build())
                .toList();
    }

    public List<FilterCinema> filterByCinema() {
        return cinemaRepository.findAll().stream().map(c -> FilterCinema.builder().cinemaId(c.getId()).cinemaName(c.getName()).build()).toList();
    }

    public List<FilterScreen> filterByScreen(Long cinemaId) {
        return screenRepository
                .findAllByCinema(cinemaRepository
                        .findById(cinemaId)
                        .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND)))
                .stream().map(s -> FilterScreen.builder().screenId(s.getId()).screenName(s.getName()).build())
                .toList();
    }
}

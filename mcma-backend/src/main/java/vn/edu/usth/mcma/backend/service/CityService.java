package vn.edu.usth.mcma.backend.service;

import constants.ApiResponseCode;
import constants.CommonStatus;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.usth.mcma.backend.exception.ApiResponse;
import vn.edu.usth.mcma.backend.exception.BusinessException;
import vn.edu.usth.mcma.backend.security.JwtHelper;
import vn.edu.usth.mcma.backend.dto.cinema.CityRequest;
import vn.edu.usth.mcma.backend.repository.CityRepository;
import vn.edu.usth.mcma.backend.entity.City;

import java.time.Instant;
import java.util.List;


@Transactional
@Service
@AllArgsConstructor
public class CityService {
    private final CityRepository cityRepository;
    private final JwtHelper jwtHelper;
    public ApiResponse createCity(CityRequest request) {
        Long userId = jwtHelper.getIdUserRequesting();
        City city = new City();
        city.setName(request.getName());
        city.setStatus(CommonStatus.ACTIVE.getStatus());
        city.setCreatedBy(userId);
        city.setLastModifiedBy(userId);
        cityRepository.save(city);
        return ApiResponse.ok();
    }
    public List<City> findAll(String query, Pageable pageable) {
        return cityRepository.findAllByNameContaining(query, pageable);
    }
    public ApiResponse updateCity(Long id, CityRequest request) {
        Long userId = jwtHelper.getIdUserRequesting();
        City city = cityRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND));
        city.setName(request.getName());
        city.setLastModifiedBy(userId);
        city.setLastModifiedDate(Instant.now());
        cityRepository.save(city);
        return ApiResponse.ok();
    }
    public ApiResponse deleteCity(Long id) {
        Long userId = jwtHelper.getIdUserRequesting();
        City city = cityRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND));
        city.setStatus(CommonStatus.INACTIVE.getStatus());
        city.setLastModifiedBy(userId);
        city.setLastModifiedDate(Instant.now());
        cityRepository.save(city);
        return ApiResponse.ok();
    }
}

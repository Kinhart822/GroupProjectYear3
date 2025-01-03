package vn.edu.usth.mcma.backend.service;

import constants.CommonStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.usth.mcma.backend.exception.ApiResponse;
import vn.edu.usth.mcma.backend.security.JwtUtil;
import vn.edu.usth.mcma.backend.dto.CityRequest;
import vn.edu.usth.mcma.backend.repository.CityRepository;
import vn.edu.usth.mcma.backend.entity.City;

import java.time.Instant;
import java.util.List;


@Transactional
@Service
public class CityService extends AbstractService<City, Long> {
    private final CityRepository cityRepository;
    private final JwtUtil jwtUtil;

    public CityService(CityRepository cityRepository, JwtUtil jwtUtil) {
        super(cityRepository);
        this.cityRepository = cityRepository;
        this.jwtUtil = jwtUtil;
    }
    public ApiResponse createCity(CityRequest request) {
        Long userId = jwtUtil.getUserIdFromToken();
        City city = new City();
        city.setName(request.getName());
        city.setStatus(CommonStatus.ACTIVE.getStatus());
        city.setCreatedBy(userId);
        city.setLastModifiedBy(userId);
        cityRepository.save(city);
        return ApiResponse.success();
    }
    public List<City> findAll(String query, Pageable pageable) {
        return cityRepository.findAllByNameContaining(query, pageable);
    }
    public ApiResponse updateCity(Long id, CityRequest request) {
        Long userId = jwtUtil.getUserIdFromToken();
        City city = findById(id);
        city.setName(request.getName());
        city.setLastModifiedBy(userId);
        city.setLastModifiedDate(Instant.now());
        cityRepository.save(city);
        return ApiResponse.success();
    }
    public ApiResponse deleteCity(Long id) {
        Long userId = jwtUtil.getUserIdFromToken();
        City city = findById(id);
        city.setStatus(CommonStatus.DELETED.getStatus());
        city.setLastModifiedBy(userId);
        city.setLastModifiedDate(Instant.now());
        cityRepository.save(city);
        return ApiResponse.success();
    }
}

package vn.edu.usth.mcma.backend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import constants.ApiResponseCode;
import vn.edu.usth.mcma.backend.exception.ApiResponse;
import vn.edu.usth.mcma.backend.exception.BusinessException;

@AllArgsConstructor
@Transactional
public abstract class AbstractService<T, ID> {
    private final JpaRepository<T, ID> repository;
    public T findById(ID id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND));
    }
    ApiResponse successResponse() {
        return ApiResponse
                .builder()
                .status(ApiResponseCode.SUCCESS.getStatus())
                .message(ApiResponseCode.SUCCESS.getMessage())
                .build();
    }
}

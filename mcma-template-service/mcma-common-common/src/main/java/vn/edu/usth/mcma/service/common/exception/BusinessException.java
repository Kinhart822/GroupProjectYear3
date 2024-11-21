package vn.edu.usth.mcma.service.common.exception;

import lombok.Getter;
import vn.edu.usth.mcma.service.common.constants.ApiResponseCode;
@Getter
public class BusinessException extends RuntimeException {
    private final String status;
    private final String message;
    public BusinessException(ApiResponseCode apiResponseCode) {
        this.status = apiResponseCode.getStatus();
        this.message = apiResponseCode.getMessage();
    }
}

package vn.edu.usth.mcma.service.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.usth.mcma.service.common.constants.ApiResponseCode;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse {

    private String status;
    private String message;

    public static CommonResponse successResponse() {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setStatus(ApiResponseCode.SUCCESS.getStatus());
        commonResponse.setMessage(ApiResponseCode.SUCCESS.getMessage());
        return commonResponse;
    }
}
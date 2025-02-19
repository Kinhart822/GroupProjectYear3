package vn.edu.usth.mcma.frontend.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendOtpRequest {
    private String sessionId;
    private String email;
}

package vn.edu.usth.mcma.backend.dto.unsorted;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminPresentation {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private Integer sex;
    private Instant dateOfBirth;
    private String phone;
    private String address;
    private Integer status;
    private Long createdBy;
    private Long lastModifiedBy;
    private Instant createdDate;
    private Instant lastModifiedDate;
}

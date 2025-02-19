package vn.edu.usth.mcma.backend.dto.unsorted;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketPresentation {
    private Long id;
    private String type;
    private String description;
    private Double price;
}

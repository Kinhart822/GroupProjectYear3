package vn.edu.usth.mcma.frontend.model.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationItem {
    private Long id;
    private String title;
    private String content;
    private String createdDate;
}

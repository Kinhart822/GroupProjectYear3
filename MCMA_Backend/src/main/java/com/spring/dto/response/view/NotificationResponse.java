package com.spring.dto.response.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private List<String> message;
    private List<String> dateCreated;
//    private String message;
//    private String dateCreated;
}

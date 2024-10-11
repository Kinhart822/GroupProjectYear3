package com.spring.dto.Response.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewCityResponse {
    private List<Integer> cityIds;
    private List<String> cityNameList;
}

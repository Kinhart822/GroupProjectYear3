package com.spring.service.impl;

import com.spring.dto.response.SearchMovieByGenreResponse;
import com.spring.dto.response.SearchMovieByNameResponse;
import com.spring.enums.PerformerSex;
import com.spring.enums.PerformerType;
import com.spring.repository.MovieRepository;
import com.spring.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<SearchMovieByNameResponse> getAllMovies(String title, Integer limit, Integer offset) {
        List<Object[]> results = movieRepository.getAllMovies(title, limit, offset);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return results.stream()
                .map(result -> new SearchMovieByNameResponse(
                        (Integer) result[0],        // id
                        (String) result[1],         // name
                        (Integer) result[2],        // length
                        (String) result[3],         // description
                        (String) result[4],         // image Url
                        (String) result[5],         // backgroundImageUrl
                        (String) result[6],         // trailerLink
                        result[7] != null ? dateFormat.format((Date) result[7]) : null, //  datePublish
                        result[8] != null ? Arrays.asList(result[8].toString().split(",")) : new ArrayList<>(), // ratingNameList
                        result[9] != null ? Arrays.asList(result[9].toString().split(",")) : new ArrayList<>(), // ratingDescriptionList
                        result[10] != null ? Arrays.asList(result[10].toString().split(",")) : new ArrayList<>(), // genreNameList
                        result[11] != null ? Arrays.asList(result[11].toString().split(",")) : new ArrayList<>(), // performerNameList
                        result[12] != null ? Arrays.asList(result[12].toString().split(",")) : new ArrayList<>(), // performerType
                        result[13] != null ? Arrays.asList(result[13].toString().split(",")) : new ArrayList<>() // performerSex
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<SearchMovieByGenreResponse> getAllMoviesByMovieGenreSet(Integer movieGenreId) {
        List<Object[]> results = movieRepository.getAllMoviesByMovieGenreSet(movieGenreId);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return results.stream()
                .map(result -> new SearchMovieByGenreResponse(
                        (Integer) result[0],        // id
                        (String) result[1],         // name
                        (Integer) result[2],        // length
                        (String) result[3],         // description
                        (String) result[4],         // image Url
                        (String) result[5],         // backgroundImageUrl
                        (String) result[6],         // trailerLink
                        result[7] != null ? dateFormat.format((Date) result[7]) : null, //  datePublish
                        result[8] != null ? Arrays.asList(result[8].toString().split(",")) : new ArrayList<>(), // ratingNameList
                        result[9] != null ? Arrays.asList(result[9].toString().split(",")) : new ArrayList<>(), // ratingDescriptionList
                        result[10] != null ? Arrays.asList(result[10].toString().split(",")) : new ArrayList<>(), // genreNameList
                        result[11] != null ? Arrays.asList(result[11].toString().split(",")) : new ArrayList<>(), // performerNameList
                        result[12] != null ? Arrays.asList(result[12].toString().split(",")) : new ArrayList<>(), // performerType
                        result[13] != null ? Arrays.asList(result[13].toString().split(",")) : new ArrayList<>() // performerSex
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<SearchMovieByGenreResponse> getAllMoviesByMovieGenreName(String name, Integer limit, Integer offset) {
        List<Object[]> results = movieRepository.getAllMoviesByMovieGenreName(name, limit, offset);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return results.stream()
                .map(result -> new SearchMovieByGenreResponse(
                        (Integer) result[0],        // id
                        (String) result[1],         // name
                        (Integer) result[2],        // length
                        (String) result[3],         // description
                        (String) result[4],         // image Url
                        (String) result[5],         // backgroundImageUrl
                        (String) result[6],         // trailerLink
                        result[7] != null ? dateFormat.format((Date) result[7]) : null, //  datePublish
                        result[8] != null ? Arrays.asList(result[8].toString().split(",")) : new ArrayList<>(), // ratingNameList
                        result[9] != null ? Arrays.asList(result[9].toString().split(",")) : new ArrayList<>(), // ratingDescriptionList
                        result[10] != null ? Arrays.asList(result[10].toString().split(",")) : new ArrayList<>(), // genreNameList
                        result[11] != null ? Arrays.asList(result[11].toString().split(",")) : new ArrayList<>(), // performerNameList
                        result[12] != null ? Arrays.asList(result[12].toString().split(",")) : new ArrayList<>(), // performerType
                        result[13] != null ? Arrays.asList(result[13].toString().split(",")) : new ArrayList<>() // performerSex
                ))
                .collect(Collectors.toList());    }
}

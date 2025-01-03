package vn.edu.usth.mcma.backend.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.edu.usth.mcma.backend.dto.CinemaProjection;
import vn.edu.usth.mcma.backend.dto.CinemaRequest;
import vn.edu.usth.mcma.backend.exception.ApiResponse;
import vn.edu.usth.mcma.backend.service.CinemaService;
import vn.edu.usth.mcma.backend.entity.Cinema;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class CinemaController {
    private final CinemaService cinemaService;
    @PostMapping("/cinema")
    public ApiResponse createCinema(@RequestBody CinemaRequest request) {
        return cinemaService.createCinema(request);
    }
    @GetMapping("/cinema")
    public List<CinemaProjection> findAll(@RequestParam(required = false, defaultValue = "") String query) {
        return cinemaService.findAll(query);
    }
    @GetMapping("/cinema/{id}")
    public Cinema findById(@PathVariable Long id) {
        return cinemaService.findById(id);
    }
    @PutMapping("/cinema/{id}")
    public ApiResponse updateCinema(@PathVariable Long id, @RequestBody CinemaRequest request) {
        return cinemaService.updateCinema(id, request);
    }
    @DeleteMapping("/cinema/{id}")
    public ApiResponse deleteCinema(@PathVariable Long id) {
        return cinemaService.deleteCinema(id);
    }
}

package vn.edu.usth.mcma.backend.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import vn.edu.usth.mcma.backend.dto.SeatMapRequest;
import vn.edu.usth.mcma.backend.entity.Seat;
import vn.edu.usth.mcma.backend.exception.ApiResponse;
import vn.edu.usth.mcma.backend.service.SeatService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;
    @PostMapping("/seat")
    public ApiResponse createSeat(@Valid @RequestBody SeatMapRequest request, HttpServletRequest hsRequest) {
        return seatService.createSeat(request, hsRequest);
    }
    @GetMapping("/seat")
    public List<Seat> findAll(@RequestParam String query, @PageableDefault Pageable pageable) {
        return seatService.findAll(query, pageable);
    }
    @GetMapping("/seat/{id}")
    public Seat findById(@PathVariable Long id) {
        return seatService.findById(id);
    }
    @PutMapping("/seat/{id}")
    public ApiResponse updateSeat(@PathVariable Long id, @RequestBody SeatMapRequest request, HttpServletRequest hsRequest) {
        return seatService.updateSeat(id, request, hsRequest);
    }
    @DeleteMapping("/seat/{id}")
    public ApiResponse deleteSeat(@PathVariable Long id, HttpServletRequest hsRequest) {
        return seatService.deleteSeat(id, hsRequest);
    }
}

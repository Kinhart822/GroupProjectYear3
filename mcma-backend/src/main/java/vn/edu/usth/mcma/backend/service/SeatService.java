package vn.edu.usth.mcma.backend.service;

import constants.ApiResponseCode;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.usth.mcma.backend.dto.SeatMapRequest;
import vn.edu.usth.mcma.backend.dto.SeatMapResponse;
import vn.edu.usth.mcma.backend.dto.SeatPosition;
import vn.edu.usth.mcma.backend.entity.Screen;
import vn.edu.usth.mcma.backend.entity.Seat;
import vn.edu.usth.mcma.backend.entity.SeatPK;
import vn.edu.usth.mcma.backend.exception.ApiResponse;
import vn.edu.usth.mcma.backend.exception.BusinessException;
import vn.edu.usth.mcma.backend.repository.ScreenRepository;
import vn.edu.usth.mcma.backend.repository.SeatRepository;

import java.util.List;

@Transactional
@Service
@AllArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final ScreenService screenService;
    private final ScreenRepository screenRepository;

    public ApiResponse createSeatMap(SeatMapRequest request) {
        Screen screen = screenRepository
                .findById(request.getScreenId())
                .orElseThrow(() -> new BusinessException(ApiResponseCode.ENTITY_NOT_FOUND));
        List<Seat> seats = request
                .getNamedSeatPositions() // sorted btw
                .stream()
                .map(pos -> Seat
                        .builder()
                        .pk(SeatPK
                                .builder()
                                .screenId(request.getScreenId())
                                .row(pos.getRow())
                                .column(pos.getCol())
                                .build())
                        .typeId(pos.getTypeId())
                        .isAvailable(true)
                        .name(pos.getName())
                        .build())
                .toList();
        seatRepository.saveAll(seats);
        return ApiResponse.success();
    }

    public SeatMapResponse findSeatMapByScreenId(Long screenId) {
        return SeatMapResponse
                .builder()
                .seatPositions(seatRepository
                        .findAllByScreenId(screenId)
                        .stream()
                        .map(s -> SeatPosition
                                .builder()
                                .row(s.getPk().getRow())
                                .col(s.getPk().getColumn())
                                .typeId(s.getTypeId())
                                .name(s.getName())
                                .build())
                        .toList())
                .build();
    }
//    public List<Seat> findAll(String query, Pageable pageable) {
//        return seatRepository.findAllByNameContaining(query, pageable);
//    }
//    public ApiResponse updateSeat(Long id, SeatMapRequest request) {
//        Long userId = jwtUtil.getUserIdFromToken(hsRequest);
//        Seat seat = findById(id);
//        // changing cinemaId is not allowed think about it :)
//        seat.setName(request.getName());
//        seat.setTypeId(request.getTypeId());
//        seat.setLastModifiedBy(userId);
//        seat.setLastModifiedDate(Instant.now());
//        seatRepository.save(seat);
//        return ApiResponse.success();
//    }
//    public ApiResponse deleteSeat(Long id) {
//        Long userId = jwtUtil.getUserIdFromToken(hsRequest);
//        Seat seat = findById(id);
//        seat.setStatus(CommonStatus.DELETED.getStatus());
//        seat.setLastModifiedBy(userId);
//        seat.setLastModifiedDate(Instant.now());
//        seatRepository.save(seat);
//        return ApiResponse.success();
//    }
}

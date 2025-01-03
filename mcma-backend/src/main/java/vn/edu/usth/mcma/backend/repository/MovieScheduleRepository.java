package vn.edu.usth.mcma.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.usth.mcma.backend.entity.MovieSchedule;

import java.time.Instant;
import java.util.List;

@Repository
public interface MovieScheduleRepository extends JpaRepository<MovieSchedule, Long> {
    @Query(nativeQuery = true, value = """
            select *
            from movie_schedule ms
            where :start between ms.start_time and ms.end_time
               or :end between ms.start_time and ms.end_time
               or :start < ms.start_time and :end > ms.end_time
            """)
    List<MovieSchedule> eventsInRange(@Param(value = "start") Instant startTime, @Param(value = "end") Instant endTime);
}
package vn.edu.usth.mcma.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.usth.mcma.backend.dto.movie.HighRatingMovieProjection;
import vn.edu.usth.mcma.backend.entity.Movie;
import vn.edu.usth.mcma.backend.entity.Review;

import java.time.Instant;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByMovieAndStatusIs(Movie movie, Integer status);
    @Query(nativeQuery = true, value = """
            select r.movie_id             as id,
                   avg(r.user_vote)       as avgVote
            from review r
                     left join movie m on r.movie_id = m.id
                     left join schedule s on m.id = s.movie_id
            where m.status = :status
              and m.release_date < :now
              and s.start_date_time > :now
            group by (s.movie_id)
            order by avgVote desc""")
    List<HighRatingMovieProjection> findHighestRatingMovies(@Param(value = "status") Integer status, @Param(value = "now") Instant now);
    @Query(nativeQuery = true, value = """
            select avg(r.user_vote) as avgVotes
            from review r
            where r.movie_id = :movieId
              and r.status = :status
            group by movie_id""")
    Double findAvgVoteByMovieIdAndStatus(Long movieId, Integer status);
}

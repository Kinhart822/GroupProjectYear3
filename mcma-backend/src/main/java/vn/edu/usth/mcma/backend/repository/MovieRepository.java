package vn.edu.usth.mcma.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.usth.mcma.backend.dto.movie.MovieDetailShortProjection;
import vn.edu.usth.mcma.backend.entity.Movie;
import vn.edu.usth.mcma.backend.entity.Schedule;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    /*
     * ====
     * User
     * ====
     */
    @Query(nativeQuery = true, value = """
            select m.id as id, m.name as name, m.length as length, m.poster as poster
            from schedule s
                     left join movie m on s.movie_id = m.id
            where s.status in :statuses
              and s.start_date_time >= :now
            group by m.id""")
    List<MovieDetailShortProjection> findAllNowShowing(@Param("statuses") List<Integer> statuses, @Param("now") Instant now);
    @Query(nativeQuery = true, value = """
            select m.id as id, m.name as name, m.length as length, m.poster as poster
            from movie m
            where m.status in :statuses
              and m.release_date >= :now
            group by m.id""")
    List<MovieDetailShortProjection> findAllComingSoon(@Param("statuses") List<Integer> statuses, @Param("now") Instant now);

    @Query(nativeQuery = true, value = """
            select m.id as id, m.name as name, m.length as length, m.poster as poster
            from map_movie_genre mg
                     left join movie m on mg.movie_id = m.id
            where mg.genre_id in :ids
              and if(:name is null, true, m.name like concat('%', :name, '%'))
            group by mg.movie_id
            having count(distinct mg.genre_id) = (select count(distinct mg2.genre_id)
                                                  from map_movie_genre mg2
                                                  where mg2.movie_id = mg.movie_id
                                                    and mg2.genre_id in :ids)
               and count(distinct mg.genre_id) = (select count(*)
                                                  from (select distinct genre_id
                                                        from map_movie_genre
                                                        where genre_id in :ids) as distinct_genres)""")
    List<MovieDetailShortProjection> findAllMovieByNameAndGenre(@Param(value = "name") String name, @Param(value = "ids") Set<Long> ids);
    @Query(nativeQuery = true, value = """
            select m.id as id, m.name as name, m.length as length, m.poster as poster
            from movie m
            where if(:name is null, true, m.name like concat('%', :name, '%'))
""")
    List<MovieDetailShortProjection> findAllMovieByName(@Param(value = "name") String name);

    List<Movie> findAllByStatusIs(Integer status);

    List<Movie> findAllByReleaseDateAfterAndStatusIs(Instant time, Integer status);

//    @Query(nativeQuery = true, value = """
//                 SELECT
//                     m.id,
//                     m.name,
//                     m.length,
//                     m.description,
//                     m.image_url,
//                     m.background_image_url,
//                     m.trailer_link,
//                     m.date_publish,
//                     GROUP_CONCAT(DISTINCT mrd.name SEPARATOR ',') AS rating_name,
//                     GROUP_CONCAT(DISTINCT mrd.description SEPARATOR ',') AS rating_description,
//                     GROUP_CONCAT(DISTINCT mgd.name SEPARATOR ',') AS genre_name,
//                     GROUP_CONCAT(DISTINCT mpd.name SEPARATOR ',') AS performer_name,
//                     GROUP_CONCAT(DISTINCT mpd.performer_type SEPARATOR ',') AS performer_type,
//                     GROUP_CONCAT(DISTINCT mpd.performer_sex SEPARATOR ',') AS performer_sex
//                 FROM movie m
//                 LEFT JOIN set_movie_rating_detail srd ON m.id = srd.movie_id
//                 LEFT JOIN movie_rating_detail mrd ON srd.movie_rating_detail_id = mrd.id
//                 LEFT JOIN set_movie_genre g ON m.id = g.movie_id
//                 LEFT JOIN movie_genre mg ON g.movie_genre_id = mg.id
//                 LEFT JOIN movie_genre_detail mgd ON mg.movie_genre_detail_id = mgd.id
//                 LEFT JOIN set_movie_performer p ON m.id = p.movie_id
//                 LEFT JOIN movie_performer mp ON p.movie_performer_id = mp.id
//                 LEFT JOIN movie_performer_detail mpd ON mp.movie_performer_detail_id = mpd.id
//                 WHERE mg.id = :movieGenreId
//                 GROUP BY m.id, m.name, m.length, m.image_url, m.trailer_link, m.date_publish
//                 ORDER BY m.date_publish DESC
//            """)
//    List<Object[]> getAllMoviesByMovieGenreSet(@Param("movieGenreId") Integer movieGenreId);
//
//    @Query(nativeQuery = true, value = """
//                SELECT
//                    m.id,
//                    m.name,
//                    m.length,
//                    m.description,
//                    m.image_url,
//                    m.background_image_url,
//                    m.trailer_link,
//                    m.date_publish,
//                    GROUP_CONCAT(DISTINCT mrd.name SEPARATOR ',') AS rating_name,
//                    GROUP_CONCAT(DISTINCT mrd.description SEPARATOR ',') AS rating_description,
//                    GROUP_CONCAT(DISTINCT mgd.name SEPARATOR ',') AS genre_name,
//                    GROUP_CONCAT(DISTINCT mpd.name SEPARATOR ',') AS performer_name,
//                    GROUP_CONCAT(DISTINCT mpd.performer_type SEPARATOR ',') AS performer_type,
//                    GROUP_CONCAT(DISTINCT mpd.performer_sex SEPARATOR ',') AS performer_sex
//                FROM movie m
//                LEFT JOIN set_movie_rating_detail srd ON m.id = srd.movie_id
//                LEFT JOIN movie_rating_detail mrd ON srd.movie_rating_detail_id = mrd.id
//                LEFT JOIN set_movie_genre g ON m.id = g.movie_id
//                LEFT JOIN movie_genre mg ON g.movie_genre_id = mg.id
//                LEFT JOIN movie_genre_detail mgd ON mg.movie_genre_detail_id = mgd.id
//                LEFT JOIN set_movie_performer p ON m.id = p.movie_id
//                LEFT JOIN movie_performer mp ON p.movie_performer_id = mp.id
//                LEFT JOIN movie_performer_detail mpd ON mp.movie_performer_detail_id = mpd.id
//                WHERE (:name IS NULL OR :name = '' OR mgd.name LIKE CONCAT('%', :name, '%'))
//                GROUP BY m.id, m.name, m.length, m.image_url, m.trailer_link, m.date_publish
//                ORDER BY m.date_publish DESC
//                LIMIT :limit OFFSET :offset
//            """)
//    List<Object[]> getAllMoviesByMovieGenreName(
//            @Param("name") String name,
//            @Param("limit") Integer limit,
//            @Param("offset") Integer offset
//    );
//
//    List<Movie> findByDatePublishBefore(Date date);
//
//    @Query("SELECT m FROM Movie m WHERE m.datePublish > :futureStartDate")
//    List<Movie> findComingSoonMovies(@Param("futureStartDate") Date futureStartDate);
//
//    @Query("SELECT m FROM Movie m " +
//            "JOIN m.movieResponds mr " +
//            "JOIN mr.rating r " +
//            "GROUP BY m.id " +
//            "HAVING AVG(r.ratingStar) BETWEEN :minRating AND :maxRating")
//    List<Movie> findHighestRatingMovies(@Param("minRating") Double minRating, @Param("maxRating") Double maxRating);
//
//    @Query("""
//                SELECT m
//                FROM Movie m
//                JOIN m.movieScheduleList ms
//                WHERE FUNCTION('DATE', ms.startTime) = :date
//            """)
//    List<Movie> findMoviesBySelectedDateSchedule(@Param("date") String date);
}
package vn.edu.usth.mcma.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie extends AbstractAuditing implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String imageUrl;
    @Column
    private Integer length;
    @Column
    private Instant publishDate;
    @Column
    private Long ratingId;
    @Column
    private String trailerUrl;
    @Column(columnDefinition = "TINYINT")
    private Integer status;
    @ManyToMany
    @JoinTable(
            name = "map_movie_performer",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "performer_id"))
    private Set<Performer> performerSet;
    @ManyToMany
    @JoinTable(
            name = "map_movie_rating",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "rating_id"))
    private Set<Rating> ratingSet;
    @ManyToMany
    @JoinTable(
            name = "map_movie_coupon",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id"))
    private Set<Coupon> couponSet;
}

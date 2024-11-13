package vn.edu.usth.mcma.service.common.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "movie")
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
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "length")
    private Integer length;
    @Column(name = "date_publish")
    private Instant datePublish;
    @Column(name = "rating_id")
    private Long ratingId;
    @Column(name = "trailer_link")
    private String trailerLink;
}

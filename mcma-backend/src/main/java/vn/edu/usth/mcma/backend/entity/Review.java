package vn.edu.usth.mcma.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(value = 1, message = "Vote cannot be less than 1")
    @Max(value = 5, message = "Vote cannot be more than 10")
    @Column(columnDefinition = "TINYINT")
    private Integer userVote;
    @Column
    private String userComment;
    @Column(columnDefinition = "TINYINT")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
    @Column
    private Long lastModifiedBy;
    @Column(updatable = false)
    private Instant createdDate;
    @Column
    private Instant lastModifiedDate;
}

package vn.edu.usth.mcma.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cinema extends AbstractAuditing implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
    @Column
    private String name;
    @Column
    private String address;
    @Column(columnDefinition = "TINYINT")
    private Integer status;
}

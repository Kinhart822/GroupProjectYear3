package vn.edu.usth.mcma.service.common.dao;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "seat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat extends AbstractAuditing implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private SeatPK id;
    private Integer typeId;
    private String name;
}

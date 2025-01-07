package vn.edu.usth.mcma.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.usth.mcma.backend.domain.Booking;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder(toBuilder = true)
public class Ticket extends AbstractAuditing implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "seat_screen_id", referencedColumnName = "screen_id"),
            @JoinColumn(name = "seat_row", referencedColumnName = "row"),
            @JoinColumn(name = "seat_col", referencedColumnName = "col")
    })
    private Seat seat;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private TicketType ticketType;
}

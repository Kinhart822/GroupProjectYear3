package vn.edu.usth.mcma.backend.domain;

import constants.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import vn.edu.usth.mcma.backend.entity.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder(toBuilder = true)
public class Booking implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String bookingNo;//server generated
    @ManyToOne
    @JoinColumn(name = "payment_method")
    private PaymentMethod paymentMethod;
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
    @CreatedDate
    @Column(updatable = false)
    private Instant createdDate;
    @LastModifiedDate
    @Column
    private Instant lastModifiedDate;
    @Column
    private Double finalPrice;
    @Column
    @Enumerated(EnumType.ORDINAL)
    private BookingStatus bookingStatus;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany
    @JoinTable(
            name = "map_booking_coupon",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id"))
    private Set<Coupon> coupons;
}



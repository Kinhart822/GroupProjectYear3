package vn.edu.usth.mcma.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User implements Serializable, UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String email;
    @Column
    @JsonIgnore
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    @JsonIgnore
    @Builder.Default
    private List<String> roles = new ArrayList<>();
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column(columnDefinition = "TINYINT")
    private Integer sex;
    @Column(name = "dob")
    private Instant dateOfBirth;
    @Column
    private String phone;
    @Column
    private String address;
    @Column
    @JsonIgnore
    private String resetKey;
    @Column
    @JsonIgnore
    private Instant resetDueDate;
    @Column(columnDefinition = "TINYINT")
    @JsonIgnore
    private Integer status;
    @Column(updatable = false)
    private Long createdBy;
    @Column
    private Long lastModifiedBy;
    @CreatedDate
    @Column(updatable = false)
    private Instant createdDate;
    @LastModifiedDate
    @Column
    private Instant lastModifiedDate;

    @ManyToMany
    @JoinTable(
            name = "map_user_coupon",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id"))
    private Set<Coupon> couponSet;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
    @Override
    public String getUsername() {
        return this.email;
    }
    @Override
    public String getPassword() {
        return this.password;
    }
}

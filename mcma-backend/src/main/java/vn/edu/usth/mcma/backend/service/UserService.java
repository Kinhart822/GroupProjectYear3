package vn.edu.usth.mcma.backend.service;

import constants.ApiResponseCode;
import constants.EntityStatus;
import constants.UserType;
import jakarta.transaction.Transactional;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.usth.mcma.backend.dao.User;
import vn.edu.usth.mcma.backend.exception.BusinessException;
import vn.edu.usth.mcma.backend.repository.UserRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService extends AbstractService<User, Long> {
    @Value("${reset-key-timeout}")
    private static int resetKeyTimeout;
    private final RandomStringGenerator numericGenerator = new RandomStringGenerator.Builder().withinRange('0', '9').get();
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }
    public boolean checkEmailExistence(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }
    public User findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new BusinessException(ApiResponseCode.EMAIL_NOT_FOUND));
    }
    public UserDetails makeUserDetailsByEmail(String email) {
        return this.getUserDetailsService().loadUserByUsername(email);
    }
    public UserDetailsService getUserDetailsService() {
        return this.userDetailsService();
    }
    private UserDetailsService userDetailsService() {
        return email -> {
            User user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new BusinessException(ApiResponseCode.EMAIL_NOT_FOUND));
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(UserType.values()[user.getUserType()].name()));
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    authorities);
        };
    }

    public Optional<User> requestPasswordReset(String email, Integer type) {
        return userRepository
                .findOneByEmailIgnoreCaseAndUserType(email, type)
                .filter(u -> u.getStatus().equals(EntityStatus.CREATED.getStatus()))
                .map(u -> {
                    u.setResetKey(numericGenerator.generate(6));
                    u.setResetDate(Instant.now());
                    return u;
                });
    }
}

package com.annie.auth.service;

import com.annie.auth.dao.UserDao;
import com.annie.auth.dto.UserRequestDto;
import com.annie.auth.entity.User;
import com.annie.common.Role;
import com.annie.security.JwtPayload;
import com.annie.security.TokenProvider;
import com.annie.security.UnauthorizedException;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

@Stateless
public class UserService {
    @Inject
    private UserDao userDAO;

    @Inject
    private TokenProvider tokenProvider;

    public User validateCredential(UserRequestDto loginRequestDTO) throws UnauthorizedException {
        Optional<User> userFound = userDAO.findByEmail(loginRequestDTO.getEmail());

        if (userFound.isEmpty()) {
            throw new UnauthorizedException("User not found");
        }
        boolean isValidPassword = BCrypt.checkpw(loginRequestDTO.getPassword(), userFound.get().getPassword());
        if (!isValidPassword) {
            throw new UnauthorizedException("Invalid password");
        }
        return userFound.get();
    }


    public String generateJWT(User user) throws Exception {
        JwtPayload payload = new JwtPayload(user.getEmail(), user.getRole().name());
        return tokenProvider.generateToken(payload);
    }
    @PostConstruct
    public void init() {
        if (userDAO.count() == 0) {
            createUser("admin@example.com", "adminpassword", Role.ADMIN);
            createUser("doctor@example.com", "doctorpassword", Role.DOCTOR);
            createUser("patient@example.com", "patientpassword", Role.PATIENT);
        }
    }

    private void createUser(String email, String password, Role role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(12))); // Mã hóa mật khẩu
        user.setRole(role);
        userDAO.save(user);
    }

}

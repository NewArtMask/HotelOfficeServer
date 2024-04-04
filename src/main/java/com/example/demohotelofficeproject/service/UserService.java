package com.example.demohotelofficeproject.service;

import com.example.demohotelofficeproject.dto.ChangePasswordDto;
import com.example.demohotelofficeproject.dto.UserDto;
import com.example.demohotelofficeproject.dto.UserLoginDto;
import com.example.demohotelofficeproject.entity.User;
import com.example.demohotelofficeproject.enums.Role;
import com.example.demohotelofficeproject.enums.UserStatus;
import com.example.demohotelofficeproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.Authentication;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public User getUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElse(null);
    }

    public User updateUser(User user) {
        if (getUserByEmail(user.getEmail()) == null) {
            throw new UsernameNotFoundException("User not found!");
        }

        return userRepository.save(user);
    }

    public User saveUser(UserDto userDto) {
        if (getUserByEmail(userDto.getEmail()) != null) {
            throw new IllegalArgumentException("User with this email is already registered!");
        }

        User user = User.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.ROLE_USER)
                .userStatus(UserStatus.CREATED)
                .build();


        return userRepository.save(user);
    }

    public User findUserByEmailAndPassword(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail()).orElse(null);

        return user != null && passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword()) ? user : null;
    }

    public User login(UserLoginDto userLoginDto) {
        User user = findUserByEmailAndPassword(userLoginDto);
        Authentication authentication;

        if (user != null) {
            authentication = new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username);
        return user.map(UserInfoDetails::new).orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found!"));
    }

    public boolean checkIfPasswordCorrect(String passwordToVerify, String savedUserPassword) {
        return passwordEncoder.matches(passwordToVerify, savedUserPassword);
    }

    public void changePassword(ChangePasswordDto changePasswordDto) {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = getUserByEmail(userEmail);

            if (checkIfPasswordCorrect(changePasswordDto.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
                userRepository.save(user);
            } else {
                throw new IllegalArgumentException("Incorrect user password!");
            }
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

//    public User changeUserStatus() {}
}

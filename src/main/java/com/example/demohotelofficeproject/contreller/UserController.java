package com.example.demohotelofficeproject.contreller;

import com.example.demohotelofficeproject.dto.ChangePasswordDto;
import com.example.demohotelofficeproject.dto.LoginResponseDto;
import com.example.demohotelofficeproject.dto.UserDto;
import com.example.demohotelofficeproject.dto.UserLoginDto;
import com.example.demohotelofficeproject.entity.User;
import com.example.demohotelofficeproject.service.JwtService;
import com.example.demohotelofficeproject.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("hoteloffice")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @GetMapping("")
    public ResponseEntity<String> getAll() {
        return ResponseEntity.ok("Hello!");
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<User> getProfile() {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.getUserByEmail(userEmail);

            return ResponseEntity.status(200).body(user);
        }

        throw new UsernameNotFoundException("Invalid user request!");
    }

    @PatchMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public  ResponseEntity<User> updateUserProfile(@RequestBody JsonPatch patch) {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.getUserByEmail(userEmail);

            try {
                User patchedUser = applyPatchToUser(patch, user);
                user = userService.updateUser(patchedUser);
            } catch (JsonPatchException | JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            return ResponseEntity.status(200).body(user);
        }

        throw new UsernameNotFoundException("Invalid user request!");
    }

    private User applyPatchToUser(
            JsonPatch patch, User targetUser) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = patch.apply(objectMapper.convertValue(targetUser, JsonNode.class));
        return objectMapper.treeToValue(patched, User.class);
    }

    @PostMapping("/registration")
    public ResponseEntity<Object> registerUser(@RequestBody UserDto userDto) {
        if (userDto == null) throw new IllegalArgumentException("User can not be null!");
        Map<String, String> registrationMessage = new HashMap<>();

        try {
            userService.saveUser(userDto);
        } catch (Exception e) {
            registrationMessage.put("message", "Failed!");
            return ResponseEntity.status(404).body(registrationMessage);
        }

        registrationMessage.put("message", "Success!");

        return ResponseEntity.status(201).body(registrationMessage);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserLoginDto userLoginDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));

        if (authentication.isAuthenticated()) {
            User user = userService.getUserByEmail(userLoginDto.getEmail());

            LoginResponseDto responseDto = LoginResponseDto.builder()
                    .userId(user.getUuid().toString())
                    .name(user.getName())
                    .surname(user.getSurname())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .userStatus(user.getUserStatus())
                    .token(jwtService.generateToken(userLoginDto.getEmail()))
                    .build();

            return ResponseEntity.status(200).body(responseDto);
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @PutMapping("/change-password")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        try {
            userService.changePassword(changePasswordDto);
            return ResponseEntity.status(200).build();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404).build();
        }
    }
}

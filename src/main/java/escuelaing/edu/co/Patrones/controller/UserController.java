package escuelaing.edu.co.Patrones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import escuelaing.edu.co.Patrones.model.User;
import escuelaing.edu.co.Patrones.service.UserService;


@CrossOrigin(origins = "https://localhost:8443", allowCredentials = "true")
@RestController
@RequestMapping("/users")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public UserController(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "Usuario registrado exitosamente";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody User user) {
        if (userService.authenticate(user.getUsername(), user.getPassword())) {
            return ResponseEntity.ok("Authenticated");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

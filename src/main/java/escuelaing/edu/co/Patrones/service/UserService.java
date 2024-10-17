package escuelaing.edu.co.Patrones.service;

import java.util.ArrayList;
import escuelaing.edu.co.Patrones.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import escuelaing.edu.co.Patrones.repository.UserRepository;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public User save(User user) {
        return userRepository.save(user); // Guardar el usuario en la base de datos
    }

    public boolean authenticate(String username, String rawPassword) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() && passwordEncoder.matches(rawPassword, user.get().getPassword());
    }
}

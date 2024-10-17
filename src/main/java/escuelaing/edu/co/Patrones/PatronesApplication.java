package escuelaing.edu.co.Patrones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import escuelaing.edu.co.Patrones.model.Property;
import escuelaing.edu.co.Patrones.model.User;
import escuelaing.edu.co.Patrones.service.PropertyService;
import escuelaing.edu.co.Patrones.service.UserService;

@SpringBootApplication
public class PatronesApplication  {

    public static void main(String[] args) {
        SpringApplication.run(PatronesApplication.class, args);
    }
}
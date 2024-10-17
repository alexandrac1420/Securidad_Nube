package escuelaing.edu.co.Patrones.repository;


import escuelaing.edu.co.Patrones.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    Page<Property> findAll(Pageable pageable);

    Page<Property> findByAddressContaining(String address, Pageable pageable);
}

package escuelaing.edu.co.Patrones.service;

import escuelaing.edu.co.Patrones.model.Property;
import escuelaing.edu.co.Patrones.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    public Page<Property> getAllProperties(Pageable pageable) {
        return propertyRepository.findAll(pageable);
    }

    public Page<Property> searchPropertiesByAddress(String address, Pageable pageable) {
        return propertyRepository.findByAddressContaining(address, pageable);
    }
    
    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
    }

    public Property updateProperty(Long id, Property propertyDetails) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));

        property.setAddress(propertyDetails.getAddress());
        property.setPrice(propertyDetails.getPrice());
        property.setSize(propertyDetails.getSize());
        property.setDescription(propertyDetails.getDescription());

        return propertyRepository.save(property);
    }

    public void deleteProperty(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
        propertyRepository.delete(property);
    }
}


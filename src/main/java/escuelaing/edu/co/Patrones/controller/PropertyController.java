package escuelaing.edu.co.Patrones.controller;

import escuelaing.edu.co.Patrones.model.Property;
import escuelaing.edu.co.Patrones.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/properties")
@CrossOrigin(origins = "https://serverfront.duckdns.org", allowCredentials = "true")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping
    public Property createProperty(@RequestBody Property property) {
        return propertyService.createProperty(property);
    }

    @GetMapping
    public Page<Property> getAllProperties(Pageable pageable) {
        return propertyService.getAllProperties(pageable);
    }


    @GetMapping("/search")
    public Page<Property> searchProperties(@RequestParam("search") String address, Pageable pageable) {
        return propertyService.searchPropertiesByAddress(address, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        Property property = propertyService.getPropertyById(id);
        return ResponseEntity.ok(property);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable Long id, @RequestBody Property propertyDetails) {
        Property updatedProperty = propertyService.updateProperty(id, propertyDetails);
        return ResponseEntity.ok(updatedProperty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok().build();
    }
}


package escuelaing.edu.co.Patrones.service;

import escuelaing.edu.co.Patrones.model.Property;
import escuelaing.edu.co.Patrones.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PropertyServiceTest {

    @InjectMocks
    private PropertyService propertyService;

    @Mock
    private PropertyRepository propertyRepository;

    private Property property1;
    private Property property2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        property1 = new Property();
        property1.setId(1L);
        property1.setAddress("Calle 1");
        property1.setPrice((double) 100000);
        property1.setSize((double) 200);
        property1.setDescription("Casa grande");

        property2 = new Property();
        property2.setId(2L);
        property2.setAddress("Calle 2");
        property2.setPrice((double) 200000);
        property2.setSize((double) 300);
        property2.setDescription("Casa pequeña");
    }

    @Test
    void testGetAllProperties() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Property> page = new PageImpl<>(Arrays.asList(property1, property2));

        when(propertyRepository.findAll(pageable)).thenReturn(page);

        Page<Property> result = propertyService.getAllProperties(pageable);

        assertEquals(2, result.getTotalElements());
        verify(propertyRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetPropertyById() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property1));

        Property result = propertyService.getPropertyById(1L);

        assertNotNull(result);
        assertEquals("Calle 1", result.getAddress());
        verify(propertyRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateProperty() {
        when(propertyRepository.save(property1)).thenReturn(property1);

        Property result = propertyService.createProperty(property1);

        assertNotNull(result);
        assertEquals("Calle 1", result.getAddress());
        verify(propertyRepository, times(1)).save(property1);
    }

    @Test
    void testUpdateProperty() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property1));
        when(propertyRepository.save(property1)).thenReturn(property1);

        Property updatedProperty = new Property();
        updatedProperty.setAddress("Nueva Calle");
        updatedProperty.setPrice((double) 150000);
        updatedProperty.setSize((double) 250);
        updatedProperty.setDescription("Casa renovada");

        Property result = propertyService.updateProperty(1L, updatedProperty);

        assertEquals("Nueva Calle", result.getAddress());
        assertEquals(150000, result.getPrice());
        verify(propertyRepository, times(1)).save(property1);
    }

    @Test
    void testDeleteProperty() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property1));

        propertyService.deleteProperty(1L);

        verify(propertyRepository, times(1)).delete(property1);
    }
}

package escuelaing.edu.co.Patrones.controller;

import escuelaing.edu.co.Patrones.model.Property;
import escuelaing.edu.co.Patrones.service.PropertyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(PropertyController.class)
public class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyService propertyService;

    @Test
    @WithMockUser  // Simular un usuario autenticado sin roles
    void testGetAllProperties() throws Exception {
        Property property1 = new Property();
        property1.setId(1L);
        property1.setAddress("Calle 1");
        property1.setPrice(100000.0);
        property1.setSize(200.0);
        property1.setDescription("Casa grande");

        Property property2 = new Property();
        property2.setId(2L);
        property2.setAddress("Calle 2");
        property2.setPrice(150000.0);
        property2.setSize(250.0);
        property2.setDescription("Casa mediana");

        Page<Property> propertyPage = new PageImpl<>(Arrays.asList(property1, property2), PageRequest.of(0, 5), 2);

        Mockito.when(propertyService.getAllProperties(Mockito.any())).thenReturn(propertyPage);

        mockMvc.perform(get("/properties?page=0&size=5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].address").value("Calle 1"))
            .andExpect(jsonPath("$.content[1].address").value("Calle 2"));
    }

    @Test
    @WithMockUser  // Simular un usuario autenticado sin roles
    void testCreateProperty() throws Exception {
        Property property = new Property();
        property.setId(1L);
        property.setAddress("Calle 1");
        property.setPrice(100000.0);
        property.setSize(200.0);
        property.setDescription("Casa grande");

        Mockito.when(propertyService.createProperty(Mockito.any())).thenReturn(property);

        String propertyJson = """
            {
                "address": "Calle 1",
                "price": 100000,
                "size": 200,
                "description": "Casa grande"
            }
            """;

        mockMvc.perform(post("/properties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(propertyJson)
            .with(csrf()))  // Incluir token CSRF
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.address").value("Calle 1"));
    }

    @Test
    @WithMockUser  // Simular un usuario autenticado sin roles
    void testUpdateProperty() throws Exception {
        Property updatedProperty = new Property();
        updatedProperty.setId(1L);
        updatedProperty.setAddress("Nueva Calle 1");
        updatedProperty.setPrice(120000.0);
        updatedProperty.setSize(210.0);
        updatedProperty.setDescription("Casa grande renovada");

        Mockito.when(propertyService.updateProperty(Mockito.eq(1L), Mockito.any())).thenReturn(updatedProperty);

        String updatedPropertyJson = """
            {
                "address": "Nueva Calle 1",
                "price": 120000,
                "size": 210,
                "description": "Casa grande renovada"
            }
            """;

        mockMvc.perform(put("/properties/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(updatedPropertyJson)
            .with(csrf()))  // Incluir token CSRF
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.address").value("Nueva Calle 1"))
            .andExpect(jsonPath("$.price").value(120000));
    }

    @Test
    @WithMockUser  // Simular un usuario autenticado sin roles
    void testDeleteProperty() throws Exception {
        Mockito.doNothing().when(propertyService).deleteProperty(1L);

        mockMvc.perform(delete("/properties/1").with(csrf()))  // Incluir token CSRF
            .andExpect(status().isOk());

        Mockito.verify(propertyService, Mockito.times(1)).deleteProperty(1L);
    }

    @Test
    @WithMockUser  // Simular un usuario autenticado sin roles
    void testSearchPropertiesByAddress() throws Exception {
        Property property1 = new Property();
        property1.setId(1L);
        property1.setAddress("Calle 1");
        property1.setPrice(100000.0);
        property1.setSize(200.0);
        property1.setDescription("Casa grande");

        Property property2 = new Property();
        property2.setId(2L);
        property2.setAddress("Calle 1");
        property2.setPrice(150000.0);
        property2.setSize(250.0);
        property2.setDescription("Casa mediana");

        Pageable pageable = PageRequest.of(0, 5);
        Page<Property> paginatedSearchResult = new PageImpl<>(Arrays.asList(property1, property2), pageable, 2);

        Mockito.when(propertyService.searchPropertiesByAddress("Calle 1", pageable)).thenReturn(paginatedSearchResult);

        mockMvc.perform(get("/properties/search?search=Calle 1&page=0&size=5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].address").value("Calle 1"))
            .andExpect(jsonPath("$.content[1].address").value("Calle 1"))
            .andExpect(jsonPath("$.content[0].description").value("Casa grande"))
            .andExpect(jsonPath("$.content[1].description").value("Casa mediana"));
    }
}


import org.example.Controladores.ControladorBase;
import org.example.Controladores.ImplementacionControladorBase;
import org.example.Entidades.EntidadBase;
import org.example.Main;
import org.example.Servicios.ImplementacionServicioBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.Serializable;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class ImplementacionControladorBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImplementacionServicioBase<EntidadBase, Long> servicio;

    private ImplementacionControladorBase controlador;

    @BeforeEach
    public void setUp() {
        controlador = new ImplementacionControladorBase();
        controlador.servicio = servicio;
    }


    @Test
    public void testGetAll() throws Exception {
        when(servicio.findAll()).thenReturn(Collections.singletonList(new EntidadBase()));

        mockMvc.perform(get("/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOne() throws Exception {
        EntidadBase entidad = new EntidadBase();
        when(servicio.findById(any(Long.class))).thenReturn(entidad);

        mockMvc.perform(get("/test/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOneNotFound() throws Exception {
        when(servicio.findById(any(Long.class))).thenReturn(null);

        mockMvc.perform(get("/test/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception {
        when(servicio.delete(any(Long.class))).thenReturn(true);

        mockMvc.perform(delete("/test/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteBadRequest() throws Exception {
        when(servicio.delete(any(Long.class))).thenThrow(new RuntimeException("Error al eliminar"));

        mockMvc.perform(delete("/test/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"Error al eliminar\"}"));
    }
}

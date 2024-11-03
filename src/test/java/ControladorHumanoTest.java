import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Controladores.ControladorHumano;
import org.example.Entidades.EntidadBase;
import org.example.Entidades.Humano;
import org.example.Entidades.Estadistica;
import org.example.Main;
import org.example.Repositorios.RepositorioEstadistica;
import org.example.Repositorios.RepositorioHumano;
import org.example.Servicios.ImplementacionServicioHumano;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.ap.internal.util.Collections;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class ControladorHumanoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImplementacionServicioHumano servicioHumano;

    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveMutante() throws Exception {
        List<String> dna = Arrays.asList("TTTCTT",
                "TTGTTT",
                "TCTTAC",
                "CGTTGT",
                "TTCTGT",
                "AGCCTA");

        // Crea la instancia de Humano
        Humano humano = new Humano();
        humano.setDna(dna);
        humano.setEsMutante(true);

        // Mockea el comportamiento del servicioHumano
        //when(servicioHumanoHumano.mutanteOno(humano)).thenReturn(true);
        when(ControladorHumanoTest.this.servicioHumano.save(any(Humano.class))).thenReturn(humano);

        // Realiza la llamada al controlador usando MockMvc
        MvcResult resultado = mockMvc.perform(post("/mutants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(humano)))
                .andReturn();

        System.out.println("Código de estado: " + resultado.getResponse().getStatus());

        // Verifica que los métodos del servicioHumano fueron llamados
        verify(ControladorHumanoTest.this.servicioHumano, times(1)).mutanteOno(any(Humano.class));
        verify(ControladorHumanoTest.this.servicioHumano, times(1)).save(any(Humano.class));
    }

    @Test
    void testSaveHumanoNoMutante() throws Exception {
        List<String> dna = Arrays.asList("GAGAGAG", "CTCTCTC", "TGCGCGT", "GAATGCG", "CCTTCCC", "TACGCGC", "GCGTACG");
        Humano humano = Humano.builder().dna(dna).build();

        when(ControladorHumanoTest.this.servicioHumano.mutanteOno(humano)).thenReturn(false);
        when(ControladorHumanoTest.this.servicioHumano.save(any(Humano.class))).thenReturn(humano);

        mockMvc.perform(post("/mutants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(humano)))
                .andExpect(status().isForbidden());

        verify(ControladorHumanoTest.this.servicioHumano, times(1)).mutanteOno(any(Humano.class));
        verify(ControladorHumanoTest.this.servicioHumano, times(1)).save(any(Humano.class));
    }

    @Test
    void testObtenerEstadisticas() throws Exception {
        Estadistica estadistica = Estadistica.builder()
                .cantidadHumanos(100L)
                .cantidadMutantes(40L)
                .ratio(0.4)
                .build();

        when(ControladorHumanoTest.this.servicioHumano.obtenerEstadistica()).thenReturn(estadistica);

        mockMvc.perform(get("/mutants/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cantidadHumanos").value(100))
                .andExpect(jsonPath("$.cantidadMutantes").value(40))
                .andExpect(jsonPath("$.ratio").value(0.4));

        verify(ControladorHumanoTest.this.servicioHumano, times(1)).obtenerEstadistica();
    }

    @Test
    public void testGetAll() throws Exception {
        when(ControladorHumanoTest.this.servicioHumano.findAll()).thenReturn(Collections.singletonList(new EntidadBase()));

        mockMvc.perform(get("/entidades")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOne() throws Exception {
        EntidadBase entidad = new EntidadBase();
        when(ControladorHumanoTest.this.servicioHumano.findById(any(Long.class))).thenReturn(entidad);

        mockMvc.perform(get("/entidades/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOneNotFound() throws Exception {
        when(ControladorHumanoTest.this.servicioHumano.findById(any(Long.class))).thenThrow(new RuntimeException("No encontrado"));

        mockMvc.perform(get("/entidades/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":\"No encontrado\"}"));
    }

    @Test
    public void testDelete() throws Exception {
        when(ControladorHumanoTest.this.servicioHumano.delete(any(Long.class))).thenReturn(true);

        mockMvc.perform(delete("/entidades/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteBadRequest() throws Exception {
        when(ControladorHumanoTest.this.servicioHumano.delete(any(Long.class))).thenThrow(new RuntimeException("Error al eliminar"));

        mockMvc.perform(delete("/entidades/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":\"Error al eliminar\"}"));
    }

    @Test
    public void testSave() throws Exception {
        EntidadBase entidad = new EntidadBase(); // Asegúrate de inicializar tu entidad correctamente
        when(ControladorHumanoTest.this.servicioHumano.save(any(EntidadBase.class))).thenReturn(entidad);

        mockMvc.perform(post("/entidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"campo\":\"valor\"}")) // Asegúrate de que esto coincida con la estructura JSON de tu entidad
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdate() throws Exception {
        EntidadBase entidad = new EntidadBase(); // Asegúrate de inicializar tu entidad correctamente
        when(ControladorHumanoTest.this.servicioHumano.update(any(Long.class), any(EntidadBase.class))).thenReturn(entidad);

        mockMvc.perform(put("/entidades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"campo\":\"nuevo valor\"}")) // Asegúrate de que esto coincida con la estructura JSON de tu entidad
                .andExpect(status().isOk());
    }
}

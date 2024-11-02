import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Controladores.ControladorHumano;
import org.example.Entidades.Humano;
import org.example.Entidades.Estadistica;
import org.example.Main;
import org.example.Servicios.ImplementacionServicioHumano;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
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
        Humano humano = Humano.builder().dna(dna).build();

        // Mockea el comportamiento del servicio
        when(servicioHumano.mutanteOno(humano)).thenReturn(true);
        when(servicioHumano.save(any(Humano.class))).thenReturn(humano);

        // Realiza la llamada al controlador usando MockMvc
        mockMvc.perform(post("/mutants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(humano))) // Asegúrate de que el objeto humano sea convertido a JSON
                .andExpect(status().isOk());

        // Verifica que los métodos del servicio fueron llamados
        verify(servicioHumano, times(1)).mutanteOno(any(Humano.class));
        verify(servicioHumano, times(1)).save(any(Humano.class));
    }

    @Test
    void testSaveHumanoNoMutante() throws Exception {
        List<String> dna = Arrays.asList("GAGAGAG", "CTCTCTC", "TGCGCGT", "GAATGCG", "CCTTCCC", "TACGCGC", "GCGTACG");
        Humano humano = Humano.builder().dna(dna).build();

        when(servicioHumano.mutanteOno(humano)).thenReturn(false);
        when(servicioHumano.save(any(Humano.class))).thenReturn(humano);

        mockMvc.perform(post("/mutants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(humano)))
                .andExpect(status().isForbidden());

        verify(servicioHumano, times(1)).mutanteOno(any(Humano.class));
        verify(servicioHumano, times(1)).save(any(Humano.class));
    }

    @Test
    void testObtenerEstadisticas() throws Exception {
        Estadistica estadistica = Estadistica.builder()
                .cantidadHumanos(100L)
                .cantidadMutantes(40L)
                .ratio(0.4)
                .build();

        when(servicioHumano.obtenerEstadistica()).thenReturn(estadistica);

        mockMvc.perform(get("/mutants/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cantidadHumanos").value(100))
                .andExpect(jsonPath("$.cantidadMutantes").value(40))
                .andExpect(jsonPath("$.ratio").value(0.4));

        verify(servicioHumano, times(1)).obtenerEstadistica();
    }
}

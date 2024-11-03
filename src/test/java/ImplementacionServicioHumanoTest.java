import org.example.Main;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.example.Entidades.Humano;
import org.example.Entidades.Estadistica;
import org.example.Repositorios.RepositorioHumano;
import org.example.Repositorios.RepositorioEstadistica;
import org.example.Servicios.ImplementacionServicioHumano;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class ImplementacionServicioHumanoTest {
    @Mock
    private RepositorioHumano repositorioHumano;

    @Mock
    private RepositorioEstadistica repositorioEstadistica;

    @InjectMocks
    private ImplementacionServicioHumano servicioHumano;

    @Test
    public void testMutanteOno() {
        Humano humano = new Humano();
        humano.setDna(List.of("AAAAGTG", "CAGTGCC", "TGATATG", "GAATTGC", "CCCTTCG", "TGACTTG", "CACTACG"));

        when(repositorioHumano.save(humano)).thenReturn(humano);
        boolean esMutante = servicioHumano.mutanteOno(humano);

        assertTrue(esMutante);
    }

    @Test
    public void testObtenerEstadistica() {
        // Configura el comportamiento del mock
        when(repositorioEstadistica.contarHumanos()).thenReturn(100L);
        when(repositorioEstadistica.contarMutantes()).thenReturn(40L);

        // Llama al método que quieres probar
        Estadistica estadistica = servicioHumano.obtenerEstadistica();

        // Verifica los resultados
        assertEquals(100L, estadistica.getCantidadHumanos());
        assertEquals(40L, estadistica.getCantidadMutantes());
        assertEquals(0.4, estadistica.getRatio(), 0.001);

        // Verifica que los métodos del mock fueron llamados
        verify(repositorioEstadistica).contarHumanos();
        verify(repositorioEstadistica).contarMutantes();
        verify(repositorioEstadistica).save(estadistica);
    }
}

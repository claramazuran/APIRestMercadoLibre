package org.example.Servicios;

import org.example.Entidades.Humano;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ServicioHumano extends ServicioBase <Humano, Long> {
//    public List<Humano> findAllMutants() throws Exception;
//    public List <Humano> findAllNoMutants() throws Exception;
    public boolean mutanteOno(Humano entity) throws Exception;
}

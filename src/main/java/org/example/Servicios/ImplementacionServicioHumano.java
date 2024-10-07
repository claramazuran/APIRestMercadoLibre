package org.example.Servicios;

import org.example.Entidades.Humano;
import org.example.Repositorios.RepositorioHumano;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ImplementacionServicioHumano extends ImplementacionServicioBase <Humano, Long> implements ServicioHumano{
    @Autowired
    private RepositorioHumano repositorioHumano;

    @Override
    public Humano save(Humano entity) {
        return repositorioHumano.save(entity);
    }

    @Override
    public boolean mutanteOno(Humano humano) {
        return humano.esMutante(humano.getDna());
    }
}

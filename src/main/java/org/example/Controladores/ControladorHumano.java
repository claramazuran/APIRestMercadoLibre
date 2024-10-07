package org.example.Controladores;

import org.example.Entidades.Humano;
import org.example.Servicios.ImplementacionServicioBase;
import org.example.Servicios.ImplementacionServicioHumano;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController //indica que es un controlador
@CrossOrigin (origins = "*")//permite dar el acceso a nuestra api desde distintos origenes o clientes, en este caso indicamos que se puede acceder desde cualquier origen
@RequestMapping(path = "/humanos")//a travez de esta uri accedemos a los metodos de entity

public class ControladorHumano extends ImplementacionControladorBase <Humano, ImplementacionServicioHumano> {
//    public ResponseEntity<?> getAllMutants();
//    public ResponseEntity<?>  getAllNoMutants();

    @PostMapping("/mutants")
    public ResponseEntity<?> save(@RequestBody Humano entity) {
        boolean esMutante = servicio.mutanteOno(entity);
        if (esMutante) {
            return ResponseEntity.status(HttpStatus.OK).body(servicio.save(entity));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"Es mutante\"}");
        }
    }

    /*@PostMapping("/mutants")
    public ResponseEntity<String> esMutanteOno(@RequestBody Humano entity) {

    }*/
}

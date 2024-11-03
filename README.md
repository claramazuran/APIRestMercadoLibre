# Proyecto de Detección de Mutantes

## Descripción

Este proyecto es una aplicación Spring Boot que permite detectar si un humano es mutante basado en su secuencia de ADN. Utiliza una arquitectura en capas con controladores, servicios y repositorios, y está diseñado para manejar solicitudes a través de una API REST.

## Estructura del Proyecto

- **Entidades**: Clases que representan los modelos de datos (por ejemplo, `Humano`, `Estadistica`).
- **Repositorios**: Interfaces que extienden `JpaRepository` para la interacción con la base de datos.
- **Servicios**: Clases que implementan la lógica de negocio y operaciones sobre las entidades.
- **Controladores**: Clases que manejan las solicitudes HTTP y responden con datos.

## Instalación

1. Clona el repositorio:

   ```bash
   git clone https://github.com/tu-usuario/nombre-del-repositorio.git
   cd nombre-del-repositorio
2. Compila el proyecto con el comando
   gradlew build
3. Ejecuta la aplicacion con el siguente comando
   gradlew bootRun

   La aplicación estará disponible en http://localhost:8080.

**##Uso**
  **##Endpoints de la API**
  POST /mutantes

      Descripción: Verifica si la secuencia de ADN enviada pertenece a un mutante.
      Cuerpo de la solicitud:
      json
      {
        "dna": ["AAAAGTG", "CAGTGCC", "TGATATG", "GAATTGC", "CCCTTCG", "TGACTTG", "CACTACG"]
      }
      Respuestas:
      200 OK: Si el humano es mutante.
      403 Forbidden: Si el humano no es mutante.
  GET /stats

    Descripción: Devuelve estadísticas sobre el número de humanos y mutantes.
    Respuesta:
    json
    {
      "cantidadHumanos": 10,
      "cantidadMutantes": 5,
      "ratio": 0.5
      
  GET /mutantes
  
    Descripción: Obtiene todas las secuencias de ADN almacenadas.
    Respuesta: Lista de todas las entidades Humano.
    
  GET /mutantes/{id}

    Descripción: Obtiene una secuencia de ADN específica por ID.
    Respuesta: Detalles de la entidad Humano correspondiente al ID proporcionado.
    
  DELETE /mutantes/{id}

    Descripción: Elimina una secuencia de ADN específica por ID.
    Respuesta: 204 No Content si la eliminación fue exitosa; 400 Bad Request si ocurrió un error.

  PUT /mutantes/{id}

    Descripción: Actualiza una secuencia de ADN específica por ID. Verifica si el humano es mutante antes de realizar la actualización.
    Cuerpo de la solicitud:
    json
    {
      "dna": ["AAAAGTG", "CAGTGCC", "TGATATG", "GAATTGC", "CCCTTCG", "TGACTTG", "CACTACG"]
    }
    Respuestas:
    200 OK: Si el humano es mutante y la actualización fue exitosa.
    403 Forbidden: Si el humano no es mutante y la actualización fue exitosa.
    500 Internal Server Error: Si ocurrió un error en la actualización.
       

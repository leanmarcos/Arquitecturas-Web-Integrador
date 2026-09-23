# ¿Por qué crear y usar ErrorDTO?
La idea es que la aplicación no se "frene" por una excepción como venimos haciendo hasta ahora.
Si el usuario comete un error en los datos, deberíamos poder informarle y que vuelva a intentar en vez de cortar la aplicación. 

## Cómo usarlo
Supongamos que un usuario pide la carrera con el id `3`. Ese id no existe en la BBDD. 

Entonces hacemos
```
ErrorResponseDTO err = new ErrorResponseDTO;
err.setErrorCode(404);
err.setErrorMessage("Carrera no encontrada");
err.setDetails("No se encontró la tarea en la base de datos");
return err;
```
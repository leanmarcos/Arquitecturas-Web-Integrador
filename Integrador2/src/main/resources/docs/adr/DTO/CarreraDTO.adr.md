# ¿Por qué CarreraResponseDTO y CarreraRequestDTO repiten código?
Si bien repiten código y es algo que hay que evitar, cumplen funciones totalmente diferentes. 

`CarreraRequestDTO`: Cumple la función de "proteger" al sistema verificando que esté todo lo que tiene que estar para crear una carrera y que los datos sean sguros

`CarreraResponseDTO`: Cumple la función de definir qué se va a mostrar al usuario, protegiendo que no se expongan datos clave

Pensé en unificarlas pero como son funciones diferentes, opté por dejarlas separadas. Así son independientes y si hay que escalarlas no es un trabajo extra.
 

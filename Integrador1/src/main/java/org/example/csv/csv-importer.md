`InputStream` → el archivo en bytes crudos, sin forma todavía. Puede venir null si no encuentra el archivo, por eso 
se chequea antes de todo.

`InputStreamReader` → agarra esos bytes y los convierte en texto legible (letras). Bytes → caracteres. Sin esto, hay 
puro binario y no se puede leer nada como texto

`CSVFormat` → la configuración de como leer el CSV. No lee nada todavía, solo dice las reglas: "la primera fila es el 
header", separador, etc.

`CSVParser` → el que agarra esa configuración (`CSVFormat`) + el texto (reader) y hace la lectura real, fila por fila. 
Es lo que se recorre con el `for`.

`CSVRecord` → una fila ya leída. De acá se saca cada columna por nombre: row.get("nombre").


Orden de la cadena: `InputStream` (bytes) → `InputStreamReader` (texto) → `CSVFormat` (reglas) → `CSVParser` (lectura real) 
→ `CSVRecord` (fila) → mapper (lambda, convierte la fila en objeto Java tipo Cliente por ejemplo).

¿Por qué `Builder`? Porque `CSVFormat` tiene un montón de opciones configurables (header, separador, etc.) y en vez 
de encadenar `.withX().withY().withZ()` (forma vieja, deprecada), se arma la configuración paso a paso con el 
`Builder`, y recién al final se cierra para tener el `CSVFormat` definitivo. Más prolijo, más rendimiento (no crea 
objetos de más en cada paso).

¿Por qué `.get()` y no `.build()`? Es solo un cambio de nombre que hicieron en una versión más nueva de la librería — .build() quedó deprecado, ahora se usa .get(). 

Por qué el `is` está afuera del `try(...)` y el resto adentro: porque is puede ser null y necesito cortar con un 
`throw` antes de seguir. Aunque `is` esté afuera, igual se cierra — porque `reader.close()` cierra en cascada al `is` que 
tiene adentro.

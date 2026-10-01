# Ejercicio Integrador
Tome como punto de partida el ejercicio integrador del TP2
1) Considere el diseño de un registro de estudiantes, con la siguiente información: nombres,
   apellido, edad, género, número de documento, ciudad de residencia, número de libreta
   universitaria, carrera(s) en la que está inscripto, antigüedad en cada una de esas carreras, y
   si se graduó o no.

2) Implementar las consultas mediante servicios REST para:
   - a) dar de alta un estudiante
   - b) matricular un estudiante en una carrera
   - c) recuperar todos los estudiantes, y especificar algún criterio de ordenamiento simple.
   - d) recuperar un estudiante, en base a su número de libreta universitaria.
   - e) recuperar todos los estudiantes, en base a su género.
   - f) recuperar las carreras con estudiantes inscriptos, y ordenar por cantidad de inscriptos.
   - g) recuperar los estudiantes de una determinada carrera, filtrado por ciudad de residencia.
   - h) generar un reporte de las carreras, que para cada carrera incluya información de los
   inscriptos y egresados por año. Se deben ordenar las carreras alfabéticamente, y
   presentar los años de manera cronológica.

3) Testear la invocación a los servicios REST mediante Postman, o cliente similar
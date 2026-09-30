# Porque se establecio como Singleton?

Crear la EntityManagerFactory es caro: lee el persistence.xml, analiza todas las entidades, 
arma el pool de conexiones y en en este caso hasta recrea las tablas por el hbm2ddl=create. Eso tiene que pasar una 
sola vez por aplicación.
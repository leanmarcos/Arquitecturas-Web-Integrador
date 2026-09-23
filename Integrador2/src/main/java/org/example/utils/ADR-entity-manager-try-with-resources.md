# ¿Por qué se puede usar try-with-resources con el EntityManager?

Desde JPA 3.1 (la versión que trae Hibernate 6.6 en este proyecto) `EntityManager` implementa `AutoCloseable`,
así que `try (EntityManager em = JPAUtil.getEntityManager()) { ... }` lo cierra solo.

Con una versión anterior de JPA no compila, y hay que cerrarlo a mano con `try/finally { em.close(); }`.

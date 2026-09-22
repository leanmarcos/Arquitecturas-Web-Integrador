package org.example;

import jakarta.persistence.EntityManager;
import org.example.utils.JPAUtil;

public class Main {
    public static void main(String[] args) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            System.out.println("Conexión OK: " + em.isOpen());
        } finally {
            JPAUtil.close();
        }
    }
}
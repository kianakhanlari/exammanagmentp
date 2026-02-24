package ir.maktab.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.function.Supplier;

public class JpaUtil {

    private static final String PERSISTENCE_UNIT_NAME = "myPersistenceUnit";
  //  private static final EntityManagerFactory emf = buildEntityManagerFactory();

    private static EntityManagerFactory emf;
    public static void initForTest(EntityManagerFactory factory) {
        emf = factory;
    }
    public static EntityManagerFactory buildEntityManagerFactory() {
        try {
            return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create EntityManagerFactory", ex);
        }
    }


    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }


    public static <E> E executeTransaction(EntityManager entityManager, Supplier<E> logicSupplier) {
        boolean shouldCommit = false;
        E result = null;

        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
                shouldCommit = true;
            }


            result = logicSupplier.get();

            if (shouldCommit) {
                entityManager.getTransaction().commit();
            }
        } catch (RuntimeException ex) {

            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw ex;
        }

        return result;
    }

}


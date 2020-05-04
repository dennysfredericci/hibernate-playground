package br.com.fredericci.playground;

import java.sql.SQLException;
import java.time.Period;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.h2.tools.Server;

import static java.lang.System.*;

public class Application {

    public static void main(String[] args) throws SQLException {
        startDatabase();

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("br.com.fredericci.pu");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        executeTest(entityManager);

        entityManager.getTransaction().commit();

        out.println("Open your browser and navigate to http://localhost:8082/");
        out.println("JDBC URL: jdbc:h2:mem:my_database");
        out.println("User Name: sa");
        out.println("Password: ");
    }

    private static void executeTest(EntityManager entityManager) {
        Person person = new Person();
        person.setFirstName("Dennys");
        person.setLastName("Fredericci");
        entityManager.persist(person);

        printPersons(entityManager);
    }

    private static void startDatabase() throws SQLException {
        new Server().runTool("-tcp", "-web", "-ifNotExists");
    }

    private static void printPersons(EntityManager entityManager) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> query = criteriaBuilder.createQuery(Person.class);
        Root<Person> from = query.from(Person.class);
        CriteriaQuery<Person> select = query.select(from);
        List<Person> persons = entityManager.createQuery(select).getResultList();

        out.println("\n\n-----------------------------------\n\n");

        persons.forEach(out::println);

        out.println("\n\n-----------------------------------\n\n");

    }
}

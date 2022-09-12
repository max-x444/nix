package nix.repository;

import lombok.NonNull;
import nix.config.HibernateFactoryUtil;
import nix.model.Student;
import nix.model.Teacher;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JPAUniversityRepository {
    private static JPAUniversityRepository instance;
    public final EntityManager entityManager;

    private JPAUniversityRepository() {
        this.entityManager = HibernateFactoryUtil.getEntityManager();
    }

    public static JPAUniversityRepository getInstance() {
        if (instance == null) {
            instance = new JPAUniversityRepository();
        }
        return instance;
    }

    public List<String> searchGroupsByName(@NonNull final String name) {
        return entityManager.createQuery("SELECT name FROM Gang WHERE name LIKE :name", String.class)
                .setParameter("name", '%' + name + '%')
                .getResultList();
    }

    public Map<String, Long> numberOfStudentsPerGroup() {
        return entityManager.createQuery("""
                        SELECT NEW map(g.name AS name, COUNT(g.id) AS count)
                        FROM Gang g
                        JOIN Student s ON g.id = s.gang.id
                        GROUP BY g.name
                        ORDER BY g.name DESC""", Map.class)
                .getResultList()
                .stream()
                .collect(Collectors.toMap(
                        x -> (String) x.get("name"),
                        y -> (Long) y.get("count"),
                        (v1, v2) -> v1, LinkedHashMap::new));
    }

    public Map<String, Double> averageScoreOfEachGroup() {
        return entityManager.createQuery("""
                        SELECT NEW map(g.name AS name, AVG(gr.value) AS avg_value)
                        FROM Gang g
                        JOIN Student s ON g.id = s.gang.id
                        JOIN Grade gr ON s.id = gr.student.id
                        GROUP BY g.name""", Map.class)
                .getResultList()
                .stream()
                .collect(Collectors.toMap(
                        x -> (String) x.get("name"),
                        y -> (Double) y.get("avg_value"),
                        (v1, v2) -> v1, LinkedHashMap::new));
    }

    public List<Teacher> searchTeacherByNameOrSurname(final String name, final String surname) {
        return entityManager.createQuery("""
                        SELECT t
                        FROM Human h
                        JOIN Teacher t ON h.id = t.id
                        WHERE h.name = :name OR h.surname = :surname""", Teacher.class)
                .setParameter("name", name)
                .setParameter("surname", surname)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public Map<String, BigDecimal> getSubjectBestAndWorstResults() {
        return ((Stream<Object[]>) entityManager.createNativeQuery("""
                        SELECT name, avg_value
                        FROM (SELECT subject_id, AVG(value) AS avg_value
                        	  FROM Grade
                        	  GROUP BY subject_id) g
                        JOIN Subject USING(subject_id)
                        WHERE avg_value = (SELECT MAX(avg_value)
                        				   FROM (SELECT subject_id, AVG(value) AS avg_value
                        	 					 FROM Grade
                        	 					 GROUP BY subject_id) g)
                           OR avg_value = (SELECT MIN(avg_value)
                        				   FROM (SELECT subject_id, AVG(value) AS avg_value
                        						 FROM Grade
                        						 GROUP BY subject_id) g)""")
                .getResultStream())
                .collect(Collectors.toMap(
                        x -> (String) x[0],
                        x -> (BigDecimal) x[1],
                        (v1, v2) -> v1, LinkedHashMap::new));
    }

    public List<Student> getStudentWhoseAverageIsGreaterThanValue(@NonNull final Double value) {
        return entityManager.createQuery("""
                SELECT s
                FROM Student s
                JOIN Human h ON s.id = h.id
                JOIN Grade gr ON s.id = gr.student.id
                GROUP BY s.id, s.name, s.surname, s.age, s.dateOfAdmission
                HAVING AVG(gr.value) > :value""", Student.class).setParameter("value", value).getResultList();
    }
}
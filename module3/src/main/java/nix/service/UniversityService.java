package nix.service;

import lombok.NonNull;
import nix.model.Student;
import nix.model.Teacher;
import nix.repository.JPAUniversityRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class UniversityService {
    private static UniversityService instance;
    private final JPAUniversityRepository jpaUniversityRepository;

    private UniversityService() {
        this.jpaUniversityRepository = JPAUniversityRepository.getInstance();
    }

    public static UniversityService getInstance() {
        if (instance == null) {
            instance = new UniversityService();
        }
        return instance;
    }

    public List<String> searchGroupsByName(@NonNull final String name) {
        return jpaUniversityRepository.searchGroupsByName(name);
    }

    public Map<String, Long> numberOfStudentsPerGroup() {
        return jpaUniversityRepository.numberOfStudentsPerGroup();
    }

    public Map<String, Double> averageScoreOfEachGroup() {
        return jpaUniversityRepository.averageScoreOfEachGroup();
    }

    public List<Teacher> searchTeacherByNameOrSurname(final String name, final String surname) {
        return jpaUniversityRepository.searchTeacherByNameOrSurname(name, surname);
    }

    public Map<String, BigDecimal> getSubjectBestAndWorstResults() {
        return jpaUniversityRepository.getSubjectBestAndWorstResults();
    }

    public List<Student> getStudentWhoseAverageIsGreaterThanValue(@NonNull final Double value) {
        return jpaUniversityRepository.getStudentWhoseAverageIsGreaterThanValue(value);
    }
}
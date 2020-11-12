package by.protasovitski.calendariba.service;

import by.protasovitski.calendariba.domain.entities.Teacher;

import java.util.List;
import java.util.NoSuchElementException;

public interface TeacherService {

    List<Teacher> findAll();

    List<Teacher> findAll(Integer pageNo,Integer pageSize,String sortBy);

    Teacher findById(Long id) throws NoSuchElementException;

    Teacher save(Teacher teacher);

    void delete(Long id);

    Teacher addTeacherDiscipline(Long teacherId, Long disciplineId);

    void delTeacherDiscipline(Long teacherId, Long disciplineId);

}

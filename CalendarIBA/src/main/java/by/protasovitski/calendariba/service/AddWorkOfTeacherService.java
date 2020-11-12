package by.protasovitski.calendariba.service;

import by.protasovitski.calendariba.domain.entities.AddWorkOfTeacher;
import by.protasovitski.calendariba.domain.entities.Group;
import by.protasovitski.calendariba.domain.entities.Teacher;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

public interface AddWorkOfTeacherService {

    List<AddWorkOfTeacher> findAllByDateIsBetween(LocalDate sDate, LocalDate fDate);
    List<AddWorkOfTeacher> findAllByDateIsBetweenAndTeacher(LocalDate sDate, LocalDate fDate, Teacher teacher);
    List<AddWorkOfTeacher> findAllByDateIsBetweenAndGroup(LocalDate sDate, LocalDate fDate, Group group);

    AddWorkOfTeacher findById(Long id) throws NoSuchElementException;

    AddWorkOfTeacher save(AddWorkOfTeacher addWorkOfTeacher);

    void delete(Long id);
}

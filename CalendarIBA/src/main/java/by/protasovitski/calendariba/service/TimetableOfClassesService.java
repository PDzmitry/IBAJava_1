package by.protasovitski.calendariba.service;

import by.protasovitski.calendariba.domain.entities.Classroom;
import by.protasovitski.calendariba.domain.entities.Group;
import by.protasovitski.calendariba.domain.entities.Teacher;
import by.protasovitski.calendariba.domain.entities.TimetableOfClasses;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfWork;
import by.protasovitski.calendariba.exceptions.DuplicationException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public interface TimetableOfClassesService {

    List<TimetableOfClasses> findAllByClassDateIsBetween(LocalDate sDate,LocalDate fDate);
    List<TimetableOfClasses> findAllByClassDateIsBetweenAndGroup(LocalDate sDate,LocalDate fDate, Group group);
    List<TimetableOfClasses> findAllByClassDateIsBetweenAndTeacher(LocalDate sDate, LocalDate fDate, Teacher teacher);
    List<TimetableOfClasses> findAllByClassDateIsBetweenAndClassroom(LocalDate sDate, LocalDate fDate, Classroom classroom);


    List<TimetableOfClasses> findAllByClassDateIsBetweenAndStatusIsTrue(LocalDate sDate, LocalDate fDate);
    List<TimetableOfClasses> findAllByClassDateIsBetweenAndGroupAndStatusIsTrue(LocalDate sDate, LocalDate fDate, Group group);
    List<TimetableOfClasses> findAllByClassDateIsBetweenAndTeacherAndStatusIsTrue(LocalDate sDate, LocalDate fDate, Teacher teacher);


    List<TimetableOfClasses> findAllByClassDateIsBetweenAndCreatedBy(LocalDate sDate,LocalDate fDate);



    void confirm(Long [] id);
    void confirm(LocalDate sDate, LocalDate fDate);
    void confirm(LocalDate sDate, LocalDate fDate, Group group);
    void confirm(LocalDate sDate, LocalDate fDate, Teacher teacher);

    void cancelConfirmation(Long [] id);
    void cancelConfirmation(LocalDate sDate, LocalDate fDate);
    void cancelConfirmation(LocalDate sDate, LocalDate fDate, Group group);
    void cancelConfirmation(LocalDate sDate, LocalDate fDate, Teacher teacher);


    TimetableOfClasses findById(Long id) throws NoSuchElementException;

    TimetableOfClasses save(TimetableOfClasses timetableOfClasses) throws DuplicationException;

    Map<TimetableOfClasses,Integer> createNeed(Long groupId);
    Map<TimetableOfClasses,Integer> createNeed(Long groupId,Long disciplineId);

    List<Classroom> getFreeClassrooms(LocalDate date, LocalTime bTime,LocalTime fTime) throws NoSuchElementException;

    void delete(Long id);
}

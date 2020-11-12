package by.protasovitski.calendariba.domain.repository;

import by.protasovitski.calendariba.domain.entities.*;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimetableOfClassesRepository extends JpaRepository<TimetableOfClasses, Long> {

    List<TimetableOfClasses> findAllByClassDateIsBetween(LocalDate sDate, LocalDate fDate);

    List<TimetableOfClasses> findAllByClassDateIsBetweenAndCreatedBy(LocalDate sDate, LocalDate fDate,String userName);

    List<TimetableOfClasses> findAllByClassDateIsBetweenAndGroup(LocalDate sDate, LocalDate fDate, Group group);

    List<TimetableOfClasses> findAllByClassDateIsBetweenAndTeacher(LocalDate sDate, LocalDate fDate, Teacher teacher);

    List<TimetableOfClasses> findAllByClassDateIsBetweenAndClassroom(LocalDate sDate, LocalDate fDate, Classroom classroom);

    List<TimetableOfClasses> findByGroupAndSubgroupAndDisciplineAndTypeOfWork(Group group, Integer subgroup, Discipline discipline, TypeOfWork typeOfWork);

    List<TimetableOfClasses> findAllByClassDateIsBetweenAndStatusIsTrue(LocalDate sDate, LocalDate fDate);
    List<TimetableOfClasses> findAllByClassDateIsBetweenAndGroupAndStatusIsTrue(LocalDate sDate, LocalDate fDate, Group group);
    List<TimetableOfClasses> findAllByClassDateIsBetweenAndTeacherAndStatusIsTrue(LocalDate sDate, LocalDate fDate, Teacher teacher);


    List<TimetableOfClasses> findAllByClassDateIsBetweenAndStatusIsFalse(LocalDate sDate, LocalDate fDate);
    List<TimetableOfClasses> findAllByClassDateIsBetweenAndGroupAndStatusIsFalse(LocalDate sDate, LocalDate fDate, Group group);
    List<TimetableOfClasses> findAllByClassDateIsBetweenAndTeacherAndStatusIsFalse(LocalDate sDate, LocalDate fDate, Teacher teacher);


    @Query("SELECT t FROM TimetableOfClasses t" +
            " WHERE t.classDate= :date" +
            " and ((t.beginTime>:bTime and t.beginTime<:fTime)" +
            " or (t.finishTime >:bTime and t.finishTime <:fTime)"+
            " or (t.beginTime < :bTime and t.finishTime >:fTime))"+
            " and t.classroom=:classroom")
    List<TimetableOfClasses> findAllByDateBetweenBTimeFTimeClassroom(
            @Param("date") LocalDate date,
            @Param("bTime") LocalTime bTime,
            @Param("fTime") LocalTime fTime,
            @Param("classroom") Classroom classroom);


    @Query("SELECT t FROM TimetableOfClasses t" +
            " WHERE t.classDate= :date" +
            " and ((t.beginTime>:bTime and t.beginTime<:fTime)" +
            " or (t.finishTime >:bTime and t.finishTime <:fTime)"+
            " or (t.beginTime < :bTime and t.finishTime >:fTime))")
    List<TimetableOfClasses> findAllByDateBetweenBTimeFTime(
            @Param("date") LocalDate date,
            @Param("bTime") LocalTime bTime,
            @Param("fTime") LocalTime fTime);
}

package by.protasovitski.calendariba.domain.repository;

import by.protasovitski.calendariba.domain.entities.AddWorkOfTeacher;
import by.protasovitski.calendariba.domain.entities.Group;
import by.protasovitski.calendariba.domain.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AddWorkOfTeacherRepository extends JpaRepository<AddWorkOfTeacher,Long> {

    List<AddWorkOfTeacher> findAllByDateIsBetween(LocalDate sDate, LocalDate fDate);
    List<AddWorkOfTeacher> findAllByDateIsBetweenAndTeacher(LocalDate sDate, LocalDate fDate, Teacher teacher);
    List<AddWorkOfTeacher> findAllByDateIsBetweenAndGroup(LocalDate sDate, LocalDate fDate, Group group);

}

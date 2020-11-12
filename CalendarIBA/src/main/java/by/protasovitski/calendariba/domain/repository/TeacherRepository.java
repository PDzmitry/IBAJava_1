package by.protasovitski.calendariba.domain.repository;

import by.protasovitski.calendariba.domain.entities.Discipline;
import by.protasovitski.calendariba.domain.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long> {
}

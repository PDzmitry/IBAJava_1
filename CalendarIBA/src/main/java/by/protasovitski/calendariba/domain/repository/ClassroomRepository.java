package by.protasovitski.calendariba.domain.repository;

import by.protasovitski.calendariba.domain.entities.Classroom;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfClassroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom,Long> {
    List<Classroom> findAllByTypeOfClassroom(TypeOfClassroom type);
}

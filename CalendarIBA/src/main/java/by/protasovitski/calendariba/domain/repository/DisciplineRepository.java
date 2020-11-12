package by.protasovitski.calendariba.domain.repository;

import by.protasovitski.calendariba.domain.entities.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline,Long> {

}

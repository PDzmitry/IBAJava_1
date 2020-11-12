package by.protasovitski.calendariba.domain.repository;

import by.protasovitski.calendariba.domain.entities.LessonPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonPlanRepository extends JpaRepository<LessonPlan,Long> {


    List<LessonPlan> findByDescriptionOfPlan_Id(Long id);

    Optional<LessonPlan> findByDescriptionOfPlanAndDiscipline(Long descriptionId,Long disciplineId);
}

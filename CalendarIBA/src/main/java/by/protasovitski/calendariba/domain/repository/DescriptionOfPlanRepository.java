package by.protasovitski.calendariba.domain.repository;

import by.protasovitski.calendariba.domain.entities.DescriptionOfPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DescriptionOfPlanRepository extends JpaRepository<DescriptionOfPlan,Long> {

}

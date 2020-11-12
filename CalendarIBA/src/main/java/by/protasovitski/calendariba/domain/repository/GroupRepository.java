package by.protasovitski.calendariba.domain.repository;

import by.protasovitski.calendariba.domain.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {
}

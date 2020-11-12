package by.protasovitski.calendariba.domain.repository;

import by.protasovitski.calendariba.domain.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
}

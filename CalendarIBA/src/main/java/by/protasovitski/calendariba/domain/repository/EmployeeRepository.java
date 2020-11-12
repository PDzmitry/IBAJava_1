package by.protasovitski.calendariba.domain.repository;

import by.protasovitski.calendariba.domain.entities.Employee;
import by.protasovitski.calendariba.domain.entities.Group;
import by.protasovitski.calendariba.domain.entities.NumberOfPersons;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfEmployment;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Optional<Employee> findByTypeOfPosition(TypeOfPosition position);
}

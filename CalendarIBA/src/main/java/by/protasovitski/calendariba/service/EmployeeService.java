package by.protasovitski.calendariba.service;

import by.protasovitski.calendariba.domain.entities.Employee;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfPosition;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> findAll();

    List<Employee> findAll(Integer pageNo, Integer pageSize, String sortBy);

   Employee findById(Long id) throws NoSuchElementException;

   Employee findByTypeOfPosition(TypeOfPosition position);

    Employee save(Employee employee);

    void delete(Long id);
}

package by.protasovitski.calendariba.service;

import by.protasovitski.calendariba.domain.entities.Department;
import by.protasovitski.calendariba.domain.entities.Teacher;

import java.util.List;
import java.util.NoSuchElementException;

public interface DepartmentService {
    List<Department> findAll();

    List<Department> findAll(Integer pageNo, Integer pageSize, String sortBy);

    Department findById(Long id) throws NoSuchElementException;

    Department save(Department department);

    void delete(Long id);
}

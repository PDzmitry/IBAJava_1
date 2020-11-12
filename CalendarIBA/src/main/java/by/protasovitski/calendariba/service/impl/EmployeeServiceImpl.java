package by.protasovitski.calendariba.service.impl;

import by.protasovitski.calendariba.domain.entities.Employee;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfPosition;
import by.protasovitski.calendariba.domain.repository.EmployeeRepository;
import by.protasovitski.calendariba.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Transactional
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepository.findAll();

    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Employee> pagedResult = employeeRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findById(Long id) throws NoSuchElementException {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employee " + id + " not found"));
    }

    @Override
    public Employee save(Employee employee) throws NoSuchElementException {
        return employeeRepository.save(employee);
    }

    @Override
    public void delete(Long id) {
        final Optional<Employee> employee =
                this.employeeRepository.findById(id);
        employee.ifPresent(this.employeeRepository::delete);
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findByTypeOfPosition(TypeOfPosition position) {
        return employeeRepository.findByTypeOfPosition(position)
                .orElseThrow(() -> new NoSuchElementException("Employee  not found"));
}
}

package by.protasovitski.calendariba.service.impl;

import by.protasovitski.calendariba.domain.entities.Department;
import by.protasovitski.calendariba.domain.repository.DepartmentRepository;
import by.protasovitski.calendariba.service.DepartmentService;
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
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Department> pagedResult = departmentRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }


    @Override
    @Transactional(readOnly = true)
    public Department findById(Long id) throws NoSuchElementException {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Department " + id + " not found"));
    }

    @Override
    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public void delete(Long id) {
        final Optional<Department> department =
                this.departmentRepository.findById(id);
        department.ifPresent(this.departmentRepository::delete);

    }
}

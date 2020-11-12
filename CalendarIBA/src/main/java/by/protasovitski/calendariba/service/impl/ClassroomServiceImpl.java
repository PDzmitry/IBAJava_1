package by.protasovitski.calendariba.service.impl;

import by.protasovitski.calendariba.domain.entities.Classroom;
import by.protasovitski.calendariba.domain.repository.ClassroomRepository;
import by.protasovitski.calendariba.service.ClassroomService;
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
public class ClassroomServiceImpl implements ClassroomService {
    private final ClassroomRepository classroomRepository;

    @Autowired
    public ClassroomServiceImpl(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Classroom> findAll() {
        return classroomRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Classroom> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Classroom> pagedResult = classroomRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }


    @Override
    @Transactional(readOnly = true)
    public Classroom findById(Long id) throws NoSuchElementException {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Classroom " + id + " not found"));
    }

    @Override
    public Classroom save(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    @Override
    public void delete(Long id) {
        final Optional<Classroom> classroom =
                this.classroomRepository.findById(id);
        classroom.ifPresent(this.classroomRepository::delete);

    }
}

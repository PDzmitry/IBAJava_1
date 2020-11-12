package by.protasovitski.calendariba.service.impl;

import by.protasovitski.calendariba.domain.entities.Discipline;
import by.protasovitski.calendariba.domain.repository.DisciplineRepository;
import by.protasovitski.calendariba.service.DisciplineService;
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
public class DisciplineServiceImpl implements DisciplineService {

    private final DisciplineRepository disciplineRepository;

    @Autowired
    public DisciplineServiceImpl(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Discipline> findAll() {
        return disciplineRepository.findAll();

    }

    @Override
    @Transactional(readOnly = true)
    public List<Discipline> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo,pageSize, Sort.by(sortBy));
        Page<Discipline> pagedResult = disciplineRepository.findAll(paging);
        if (pagedResult.hasContent()){
            return pagedResult.getContent();
        }else {
            return new ArrayList<>() ;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Discipline findById(Long id) throws NoSuchElementException {
        return disciplineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Discipline " + id + " not found"));
    }

    @Override
    public Discipline save(Discipline discipline) throws NoSuchElementException {
        return disciplineRepository.save(discipline);
    }

    @Override
    public void delete(Long id) {
        final Optional<Discipline> discipline =
                this.disciplineRepository.findById(id);
        discipline.ifPresent(this.disciplineRepository::delete);
    }

}

package by.protasovitski.calendariba.service.impl;

import by.protasovitski.calendariba.domain.entities.LessonPlan;
import by.protasovitski.calendariba.domain.repository.LessonPlanRepository;
import by.protasovitski.calendariba.service.LessonPlanService;
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
public class LessonPlanServiceImpl implements LessonPlanService {

    private final LessonPlanRepository lessonPlanRepository;

    @Autowired
    public LessonPlanServiceImpl(LessonPlanRepository lessonPlanRepository) {
        this.lessonPlanRepository = lessonPlanRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonPlan> findAll() {
        return lessonPlanRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonPlan> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<LessonPlan> pagedResult = lessonPlanRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public LessonPlan findById(Long id) throws NoSuchElementException {
        return lessonPlanRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("LessonPlan " + id + " not found"));
    }

    @Override
    public LessonPlan save(LessonPlan lessonPlan) {
        return lessonPlanRepository.save(lessonPlan);
    }

    @Override
    public void delete(Long id) {
        final Optional<LessonPlan> lessonPlan =
                this.lessonPlanRepository.findById(id);
        lessonPlan.ifPresent(this.lessonPlanRepository::delete);
    }

    @Override
    public List<LessonPlan> findAllByDescriptionOfPlan_Id(Long id) {
        return lessonPlanRepository.findByDescriptionOfPlan_Id(id);
    }
}

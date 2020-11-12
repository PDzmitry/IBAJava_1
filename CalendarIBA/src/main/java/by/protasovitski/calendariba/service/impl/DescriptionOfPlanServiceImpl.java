package by.protasovitski.calendariba.service.impl;

import by.protasovitski.calendariba.domain.entities.DescriptionOfPlan;
import by.protasovitski.calendariba.domain.repository.DescriptionOfPlanRepository;
import by.protasovitski.calendariba.service.DescriptionOfPlanService;
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
public class DescriptionOfPlanServiceImpl implements DescriptionOfPlanService {

    private final DescriptionOfPlanRepository descriptionOfPlanRepository;

    @Autowired
    public DescriptionOfPlanServiceImpl(DescriptionOfPlanRepository descriptionOfPlanRepository) {
        this.descriptionOfPlanRepository = descriptionOfPlanRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DescriptionOfPlan> findAll() {
        return descriptionOfPlanRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DescriptionOfPlan> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<DescriptionOfPlan> pagedResult = descriptionOfPlanRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DescriptionOfPlan findById(Long id) throws NoSuchElementException {
        return descriptionOfPlanRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("DescriptionOfPlan " + id + " not found"));
    }

    @Override
    public DescriptionOfPlan save(DescriptionOfPlan descriptionOfPlan) {
        return descriptionOfPlanRepository.save(descriptionOfPlan);
    }

    @Override
    public void delete(Long id) {
        final Optional<DescriptionOfPlan> descriptionOfPlan =
                this.descriptionOfPlanRepository.findById(id);
        descriptionOfPlan.ifPresent(this.descriptionOfPlanRepository::delete);
    }
}

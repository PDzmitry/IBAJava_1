package by.protasovitski.calendariba.service;

import by.protasovitski.calendariba.domain.entities.DescriptionOfPlan;
import by.protasovitski.calendariba.domain.entities.LessonPlan;

import java.util.List;
import java.util.NoSuchElementException;

public interface DescriptionOfPlanService {

    List<DescriptionOfPlan> findAll();

    List<DescriptionOfPlan> findAll(Integer pageNo, Integer pageSize, String sortBy);

    DescriptionOfPlan findById(Long id) throws NoSuchElementException;

    DescriptionOfPlan save(DescriptionOfPlan descriptionOfPlan);

    void delete(Long id);
}

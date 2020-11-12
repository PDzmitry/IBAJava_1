package by.protasovitski.calendariba.service;

import by.protasovitski.calendariba.domain.entities.LessonPlan;

import java.util.List;
import java.util.NoSuchElementException;

public interface LessonPlanService {

    List<LessonPlan> findAll();

    List<LessonPlan> findAll(Integer pageNo, Integer pageSize, String sortBy);

    LessonPlan findById(Long id) throws NoSuchElementException;

    LessonPlan save(LessonPlan lessonPlan);

    List<LessonPlan> findAllByDescriptionOfPlan_Id(Long id);

    void delete(Long id);
}

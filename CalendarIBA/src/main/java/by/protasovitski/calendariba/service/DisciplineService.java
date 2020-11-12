package by.protasovitski.calendariba.service;

import by.protasovitski.calendariba.domain.entities.Discipline;
import by.protasovitski.calendariba.domain.entities.Teacher;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface DisciplineService {

    List<Discipline> findAll();

    List<Discipline> findAll(Integer pageNo, Integer pageSize, String sortBy);

    Discipline findById(Long id) throws NoSuchElementException;

    Discipline save(Discipline discipline);

    void delete(Long id);
}

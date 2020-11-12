package by.protasovitski.calendariba.service;

import by.protasovitski.calendariba.domain.entities.Classroom;

import java.util.List;
import java.util.NoSuchElementException;

public interface ClassroomService {
    List<Classroom> findAll();

    List<Classroom> findAll(Integer pageNo, Integer pageSize, String sortBy);

    Classroom findById(Long id) throws NoSuchElementException;

    Classroom save(Classroom classroom);

    void delete(Long id);
}

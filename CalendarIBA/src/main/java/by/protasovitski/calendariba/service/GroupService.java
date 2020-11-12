package by.protasovitski.calendariba.service;

import by.protasovitski.calendariba.domain.entities.Group;
import by.protasovitski.calendariba.domain.entities.NumberOfPersons;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface GroupService {
    List<Group> findAll();

    List<Group> findAll(Integer pageNo, Integer pageSize, String sortBy);

    Group findById(Long id) throws NoSuchElementException;

    Group save(Group group);

    void delete(Long id);

    NumberOfPersons saveNumberOfPersons(NumberOfPersons number);

    void deleteNumberOfPersons(Long id_num_persons);

    List<NumberOfPersons> findByGroup(Group group);

    NumberOfPersons  findNumberOfPersonsById(Long id);

    Optional<NumberOfPersons> findNumberOfPersons(Group group, int subgroup, LocalDate date);
}

package by.protasovitski.calendariba.service.impl;

import by.protasovitski.calendariba.domain.entities.Group;
import by.protasovitski.calendariba.domain.entities.NumberOfPersons;
import by.protasovitski.calendariba.domain.repository.GroupRepository;
import by.protasovitski.calendariba.domain.repository.NumberOfPersonsRepository;
import by.protasovitski.calendariba.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service
public class GroupServiceImpl implements GroupService {

    private GroupRepository groupRepository;
    private NumberOfPersonsRepository numberOfPersonsRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, NumberOfPersonsRepository numberOfPersonsRepository) {
        this.groupRepository = groupRepository;
        this.numberOfPersonsRepository = numberOfPersonsRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Group> pagedResult = groupRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Group findById(Long id) throws NoSuchElementException {
        return groupRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Group " + id + " not found"));
    }

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public void delete(Long id) {
        final Optional<Group> group =
                this.groupRepository.findById(id);
        group.ifPresent(this.groupRepository::delete);
    }

    @Override
    public NumberOfPersons saveNumberOfPersons(NumberOfPersons number) {
        return numberOfPersonsRepository.save(number);
    }

    @Override
    public void deleteNumberOfPersons(Long id_num_persons) {
        final Optional<NumberOfPersons> numberOfPersons =
                this.numberOfPersonsRepository.findById(id_num_persons);
        numberOfPersons.ifPresent(this.numberOfPersonsRepository::delete);
    }

    @Override
    public List<NumberOfPersons> findByGroup(Group group) {
        return numberOfPersonsRepository.findByGroup(group);
    }

    @Override
    public Optional<NumberOfPersons> findNumberOfPersons(Group group, int subgroup, LocalDate date) {

        return numberOfPersonsRepository.findNumberOfPersons(group,subgroup,date);
    }

    @Override
    public NumberOfPersons findNumberOfPersonsById(Long id) {
        return numberOfPersonsRepository.findById(id).orElseThrow(() -> new NoSuchElementException("NumberOfPersons " + id + " not found"));
    }
}

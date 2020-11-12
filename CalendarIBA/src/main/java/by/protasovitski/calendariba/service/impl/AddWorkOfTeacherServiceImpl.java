package by.protasovitski.calendariba.service.impl;

import by.protasovitski.calendariba.domain.entities.AddWorkOfTeacher;
import by.protasovitski.calendariba.domain.entities.Group;
import by.protasovitski.calendariba.domain.entities.Teacher;
import by.protasovitski.calendariba.domain.repository.AddWorkOfTeacherRepository;
import by.protasovitski.calendariba.service.AddWorkOfTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class AddWorkOfTeacherServiceImpl implements AddWorkOfTeacherService {

    @Autowired
    private AddWorkOfTeacherRepository addWorkOfTeacherRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AddWorkOfTeacher> findAllByDateIsBetween(LocalDate sDate, LocalDate fDate) {
        return addWorkOfTeacherRepository.findAllByDateIsBetween(sDate,fDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddWorkOfTeacher> findAllByDateIsBetweenAndTeacher(LocalDate sDate, LocalDate fDate, Teacher teacher) {
        return addWorkOfTeacherRepository.findAllByDateIsBetweenAndTeacher(sDate,fDate,teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddWorkOfTeacher> findAllByDateIsBetweenAndGroup(LocalDate sDate, LocalDate fDate, Group group) {
        return addWorkOfTeacherRepository.findAllByDateIsBetweenAndGroup(sDate,fDate,group);
    }

    @Override
    @Transactional(readOnly = true)
    public AddWorkOfTeacher findById(Long id) throws NoSuchElementException {
        return addWorkOfTeacherRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Addition work of Teacher " + id + " not found"));
    }

    @Override
    public AddWorkOfTeacher save(AddWorkOfTeacher addWork) {
        return addWorkOfTeacherRepository.save(addWork);
    }

    @Override
    public void delete(Long id) {
        final Optional<AddWorkOfTeacher> addWork=
                this.addWorkOfTeacherRepository.findById(id);
        addWork.ifPresent(this.addWorkOfTeacherRepository::delete);

    }
}

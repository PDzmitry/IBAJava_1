package by.protasovitski.calendariba.service.impl;

import by.protasovitski.calendariba.domain.entities.Discipline;
import by.protasovitski.calendariba.domain.entities.Teacher;
import by.protasovitski.calendariba.domain.repository.DisciplineRepository;
import by.protasovitski.calendariba.domain.repository.TeacherRepository;
import by.protasovitski.calendariba.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
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


@Slf4j
@Transactional
@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final DisciplineRepository disciplineRepository;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, DisciplineRepository disciplineRepository) {
        this.teacherRepository = teacherRepository;
        this.disciplineRepository = disciplineRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> findAll() {
        return teacherRepository.findAll();

    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> findAll(Integer pageNo,Integer pageSize,String sortBy){
        Pageable paging = PageRequest.of(pageNo,pageSize, Sort.by(sortBy));
        Page<Teacher> pagedResult = teacherRepository.findAll(paging);
        if (pagedResult.hasContent()){
            return pagedResult.getContent();
        }else {
            return new ArrayList<>() ;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Teacher findById(Long id) throws NoSuchElementException{
        return teacherRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Teacher " + id + " not found"));
    }

    @Override
    public Teacher save(Teacher teacher) throws NoSuchElementException {
            return teacherRepository.save(teacher);
    }

    @Override
    public void delete(Long id) {
        final Optional<Teacher> teacher=
                this.teacherRepository.findById(id);
       teacher.ifPresent(this.teacherRepository::delete);
    }

    @Override
    public Teacher addTeacherDiscipline(Long teacherId, Long disciplineId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(()-> new NoSuchElementException("Teacher " + teacherId +" not found"));
        Discipline discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(()-> new NoSuchElementException("Discipline " + disciplineId + " not found"));
        teacher.getDisciplines().add(discipline);
        discipline.getTeachers().add(teacher);
        return teacher;
    }

    @Override
    public void delTeacherDiscipline(Long teacherId, Long disciplineId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(()-> new NoSuchElementException("Teacher " + teacherId +" not found"));
        Discipline discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(()-> new NoSuchElementException("Discipline " + disciplineId + " not found"));
        teacher.getDisciplines().remove(discipline);
        discipline.getTeachers().remove(teacher);
    }
}

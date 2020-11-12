package by.protasovitski.calendariba.service.impl;

import by.protasovitski.calendariba.domain.entities.*;
import by.protasovitski.calendariba.domain.repository.*;
import by.protasovitski.calendariba.exceptions.DuplicationException;
import by.protasovitski.calendariba.exceptions.OwnerVerificationException;
import by.protasovitski.calendariba.service.TimetableOfClassesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Transactional
@Service
@Slf4j
public class TimetableOfClassesServiceImpl implements TimetableOfClassesService {

    private final TimetableOfClassesRepository timetableOfClassesRepository;
    private final GroupRepository groupRepository;
    private final DisciplineRepository disciplineRepository;
    private final LessonPlanRepository lessonPlanRepository;
    private final ClassroomRepository classroomRepository;
    private final Long timeInterval = 45L;


    @Autowired
    public TimetableOfClassesServiceImpl(TimetableOfClassesRepository timetableOfClassesRepository, GroupRepository groupRepository, DisciplineRepository disciplineRepository, LessonPlanRepository lessonPlanRepository, ClassroomRepository classroomRepository) {
        this.timetableOfClassesRepository = timetableOfClassesRepository;
        this.groupRepository = groupRepository;
        this.disciplineRepository = disciplineRepository;
        this.lessonPlanRepository = lessonPlanRepository;
        this.classroomRepository = classroomRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimetableOfClasses> findAllByClassDateIsBetween(LocalDate startClassDate, LocalDate endClassDate) {
        return timetableOfClassesRepository.findAllByClassDateIsBetween(startClassDate, endClassDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimetableOfClasses> findAllByClassDateIsBetweenAndCreatedBy(LocalDate sDate, LocalDate fDate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        return timetableOfClassesRepository.findAllByClassDateIsBetweenAndCreatedBy(sDate,fDate,userName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimetableOfClasses> findAllByClassDateIsBetweenAndGroup(LocalDate sDate, LocalDate fDate, Group group) {
        return timetableOfClassesRepository.findAllByClassDateIsBetweenAndGroup(sDate, fDate, group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimetableOfClasses> findAllByClassDateIsBetweenAndTeacher(LocalDate sDate, LocalDate fDate, Teacher teacher) {
        return timetableOfClassesRepository.findAllByClassDateIsBetweenAndTeacher(sDate, fDate, teacher);
    }

    @Override
    public List<TimetableOfClasses> findAllByClassDateIsBetweenAndClassroom(LocalDate sDate, LocalDate fDate, Classroom classroom) {
        return timetableOfClassesRepository.findAllByClassDateIsBetweenAndClassroom(sDate, fDate, classroom);
    }

    @Override
    @Transactional(readOnly = true)
    public TimetableOfClasses findById(Long id) throws NoSuchElementException {
        return timetableOfClassesRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("TimetableOfClasses " + id + " not found"));
    }

    @Override
    public TimetableOfClasses save(TimetableOfClasses timetableOfClasses) throws DuplicationException {
        if (!(timetableOfClasses.getId() == null)) {
            Optional<TimetableOfClasses> timetable = timetableOfClassesRepository.findById(timetableOfClasses.getId());
            timetable.ifPresent(this::ownerVerification);
        }
        List<TimetableOfClasses> timetable;
        timetable = timetableOfClassesRepository.findAllByDateBetweenBTimeFTimeClassroom(
                timetableOfClasses.getClassDate(), timetableOfClasses.getBeginTime(),
                timetableOfClasses.getFinishTime(), timetableOfClasses.getClassroom());
        long counter = timetable.stream().filter(t -> t.getId().equals(timetableOfClasses.getId())).count();
        if (!timetable.isEmpty() && counter == 0) {

            log.error("Classes in the classroom:" + timetableOfClasses.getClassroom().getNumber() + " are already being held at this time ");
            throw new DuplicationException("Classes in the classroom are already being held at this time");
        }


        return timetableOfClassesRepository.save(timetableOfClasses);
    }

    @Override
    public void delete(Long id) {

        final Optional<TimetableOfClasses> timetable =
                this.timetableOfClassesRepository.findById(id);
        timetable.ifPresent(this::ownerVerification);
        timetable.ifPresent(this.timetableOfClassesRepository::delete);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<TimetableOfClasses, Integer> createNeed(Long groupId) {
        Map<TimetableOfClasses, Integer> map = new HashMap<>();
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent()) {
            lessonPlanRepository.findByDescriptionOfPlan_Id(
                    group.get().getDescriptionOfPlan().getId())
                    .stream()
                    .forEach(l -> {
                        int subgroup = group.get().getNumberOfSubgroup();
                        IntStream.rangeClosed(1, subgroup)
                                .forEach(s -> {
                                    int counter = l.getNumberOfHours() -
                                            timetableOfClassesRepository
                                                    .findByGroupAndSubgroupAndDisciplineAndTypeOfWork(
                                                            group.get(),
                                                            s,
                                                            disciplineRepository.findById(l.getDiscipline().getId()).get(),
                                                            l.getTypeOfWork())
                                                    .size();
                                    if (counter > 0) {
                                        TimetableOfClasses timetable = new TimetableOfClasses();
                                        timetable.setDiscipline(l.getDiscipline());
                                        timetable.setGroup(group.get());
                                        timetable.setSubgroup(s);
                                        timetable.setTypeOfWork(l.getTypeOfWork());
                                        timetable.setTeacher(l.getTeacher());
                                        map.put(timetable, counter);
                                    }
                                });
                    });
        }
        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<TimetableOfClasses, Integer> createNeed(Long groupId, Long disciplineId) {
        Map<TimetableOfClasses, Integer> map = new HashMap<>();
        Optional<Group> group = groupRepository.findById(groupId);
        Optional<Discipline> discipline = disciplineRepository.findById(disciplineId);
        if (group.isPresent() && discipline.isPresent()) {
            lessonPlanRepository.findByDescriptionOfPlan_Id(
                    group.get().getDescriptionOfPlan().getId())
                    .stream()
                    .filter(d -> d.getDiscipline() == discipline.get())
                    .forEach(l -> {
                        int subgroup = group.get().getNumberOfSubgroup();
                        IntStream.rangeClosed(1, subgroup)
                                .forEach(s -> {
                                    int counter = l.getNumberOfHours() -
                                            timetableOfClassesRepository
                                                    .findByGroupAndSubgroupAndDisciplineAndTypeOfWork(
                                                            group.get(),
                                                            s,
                                                            disciplineRepository.findById(l.getDiscipline().getId()).get(),
                                                            l.getTypeOfWork())
                                                    .size();
                                    if (counter > 0) {
                                        TimetableOfClasses timetable = new TimetableOfClasses();
                                        timetable.setDiscipline(l.getDiscipline());
                                        timetable.setGroup(group.get());
                                        timetable.setSubgroup(s);
                                        timetable.setTypeOfWork(l.getTypeOfWork());
                                        timetable.setTeacher(l.getTeacher());
                                        map.put(timetable, counter);
                                    }
                                });
                    });
        }
        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Classroom> getFreeClassrooms(LocalDate date, LocalTime bTime, LocalTime fTime) throws NoSuchElementException {

        List<Classroom> classrooms = classroomRepository.findAll();
        Set<Classroom> set = timetableOfClassesRepository.findAllByDateBetweenBTimeFTime(date, bTime, fTime)
                .stream()
                .map(TimetableOfClasses::getClassroom)
                .collect(Collectors.toSet());
        classrooms.removeAll(set);

        return classrooms;
    }

    private void ownerVerification(TimetableOfClasses timetable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!auth.getName().equals(timetable.getCreatedBy())) {
            log.info("Owner: " + timetable.getCreatedBy());
            log.error("You do not have permission to perform this operation");
            throw new OwnerVerificationException("You do not have permission to perform this operation");
        }
    }

    @Override
    public List<TimetableOfClasses> findAllByClassDateIsBetweenAndStatusIsTrue(LocalDate sDate, LocalDate fDate) {
        return timetableOfClassesRepository.findAllByClassDateIsBetweenAndStatusIsTrue(sDate, fDate);
    }

    @Override
    public List<TimetableOfClasses> findAllByClassDateIsBetweenAndGroupAndStatusIsTrue(LocalDate sDate, LocalDate fDate, Group group) {
        return timetableOfClassesRepository.findAllByClassDateIsBetweenAndGroupAndStatusIsTrue(sDate, fDate, group);
    }

    @Override
    public List<TimetableOfClasses> findAllByClassDateIsBetweenAndTeacherAndStatusIsTrue(LocalDate sDate, LocalDate fDate, Teacher teacher) {
        return timetableOfClassesRepository.findAllByClassDateIsBetweenAndTeacherAndStatusIsTrue(sDate, fDate, teacher);
    }

    //Подтверждение занятий по массиву id
    @Override
    public void confirm(Long[] id) {
        List<TimetableOfClasses> timetable =
                Arrays.stream(id)
                        .map(t -> timetableOfClassesRepository.findById(t)
                                .orElseThrow(() -> new NoSuchElementException("TimetableOfClasses " + t + " not found")))
                        .collect(Collectors.toList());
        timetable.stream()
                .filter(t -> !t.getReserved())
                .forEach(record ->
                {
                    record.setStatus(true);
                    save(record);
                });
    }

    //Подтверждение занятий за период
    @Override
    public void confirm(LocalDate sDate, LocalDate fDate) {
        List<TimetableOfClasses> timetable = timetableOfClassesRepository.findAllByClassDateIsBetweenAndStatusIsFalse(sDate, fDate);
        timetable.stream()
                .filter(t -> !t.getReserved())
                .forEach(record ->
                {
                    record.setStatus(true);
                    save(record);
                });

    }

    //Подтверждение занятий за период по группе
    @Override
    public void confirm(LocalDate sDate, LocalDate fDate, Group group) {
        List<TimetableOfClasses> timetable = timetableOfClassesRepository.findAllByClassDateIsBetweenAndGroupAndStatusIsFalse(sDate, fDate, group);
        timetable.stream()
                .filter(t -> !t.getReserved())
                .forEach(record ->
                {
                    record.setStatus(true);
                    save(record);
                });
    }

    //Подтверждение занятий за период по преподавателю
    @Override
    public void confirm(LocalDate sDate, LocalDate fDate, Teacher teacher) {
        List<TimetableOfClasses> timetable = timetableOfClassesRepository.findAllByClassDateIsBetweenAndTeacherAndStatusIsFalse(sDate, fDate, teacher);
        timetable.stream()
                .filter(t -> !t.getReserved())
                .forEach(record ->
                {
                    record.setStatus(true);
                    save(record);
                });

    }

    //Отмена подтверждения занятий по массиву id
    @Override
    public void cancelConfirmation(Long[] id) {
        List<TimetableOfClasses> timetable =
                Arrays.stream(id)
                        .map(t -> timetableOfClassesRepository.findById(t)
                                .orElseThrow(() -> new NoSuchElementException("TimetableOfClasses " + t + " not found")))
                        .collect(Collectors.toList());
        timetable.stream()
                .filter(t -> !t.getReserved())
                .forEach(record ->
                {
                    record.setStatus(false);
                    save(record);
                });
    }

    //Отмена подтверждения занятий за период
    @Override
    public void cancelConfirmation(LocalDate sDate, LocalDate fDate) {
        List<TimetableOfClasses> timetable = timetableOfClassesRepository.findAllByClassDateIsBetweenAndStatusIsTrue(sDate, fDate);
        timetable.stream()
                .filter(t -> !t.getReserved())
                .forEach(record ->
                {
                    record.setStatus(false);
                    save(record);
                });

    }

    //Отмена подтверждения занятий за период по группе
    @Override
    public void cancelConfirmation(LocalDate sDate, LocalDate fDate, Group group) {
        List<TimetableOfClasses> timetable = timetableOfClassesRepository.findAllByClassDateIsBetweenAndGroupAndStatusIsTrue(sDate, fDate, group);
        timetable.stream()
                .filter(t -> !t.getReserved())
                .forEach(record ->
                {
                    record.setStatus(false);
                    save(record);
                });

    }

    //Отмена подтверждения занятий за период по преподавателю
    @Override
    public void cancelConfirmation(LocalDate sDate, LocalDate fDate, Teacher teacher) {
        List<TimetableOfClasses> timetable = timetableOfClassesRepository.findAllByClassDateIsBetweenAndTeacherAndStatusIsTrue(sDate, fDate, teacher);
        timetable.stream()
                .filter(t -> !t.getReserved())
                .forEach(record ->
                {
                    record.setStatus(false);
                    save(record);
                });
    }
}

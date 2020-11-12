package by.protasovitski.calendariba.report.impl;

import by.protasovitski.calendariba.aop.logging.LogMethod;
import by.protasovitski.calendariba.domain.entities.*;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfAddWork;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfCourse;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfEmployment;
import by.protasovitski.calendariba.domain.entities.enums.TypeOfWork;
import by.protasovitski.calendariba.exceptions.ResourceNotFoundException;
import by.protasovitski.calendariba.report.TeacherReportService;
import by.protasovitski.calendariba.service.AddWorkOfTeacherService;
import by.protasovitski.calendariba.service.GroupService;
import by.protasovitski.calendariba.service.TeacherService;
import by.protasovitski.calendariba.service.TimetableOfClassesService;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class TeacherReportServiceImpl implements TeacherReportService {


    private TeacherService teacherService;
    private TimetableOfClassesService timetableOfClassesService;
    private AddWorkOfTeacherService addWorkOfTeacherService;
    private GroupService groupService;
    private final double TARIFF = 0.2;

    @Autowired
    public TeacherReportServiceImpl(TeacherService teacherService,
                                    TimetableOfClassesService timetableOfClassesService,
                                    AddWorkOfTeacherService addWorkOfTeacherService,
                                    GroupService groupService) {
        this.teacherService = teacherService;
        this.timetableOfClassesService = timetableOfClassesService;
        this.addWorkOfTeacherService = addWorkOfTeacherService;
        this.groupService = groupService;
    }


    @Value(value = "ru_Ru")
    private Locale locale;


    @Override
    public JRDataSource create() {
        List<Map<String, Object>> result = new ArrayList<>();
        teacherService.findAll()
                .forEach(teacher -> {
                    Set<Discipline> disciplines = teacher.getDisciplines();
                    if (disciplines.isEmpty()) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("teacher", teacher.getLastName()
                                + " " + teacher.getFirstName()
                                + " " + teacher.getPatronymic());
                        item.put("type_employee", getTypeEmployee(teacher.getTypeOfEmployment()));
                        item.put("discipline", "");
                        result.add(item);
                    }
                    disciplines
                            .forEach(discipline -> {
                                Map<String, Object> item = new HashMap<>();
                                item.put("teacher", teacher.getLastName()
                                        + " " + teacher.getFirstName()
                                        + " " + teacher.getPatronymic());
                                item.put("type_employee", getTypeEmployee(teacher.getTypeOfEmployment()));
                                item.put("discipline", discipline.getDisciplineName());
                                result.add(item);

                            });
                });
        return new JRBeanCollectionDataSource(result);
    }

    @Override
    public JRDataSource create(LocalDate begin, LocalDate end) {
        Map<Teacher, Map<Discipline, Map<Group, Map<Integer, Map<TypeOfWork, Long>>>>> teacherWork =
                timetableOfClassesService.findAllByClassDateIsBetweenAndStatusIsTrue(begin, end)
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        TimetableOfClasses::getTeacher,
                                        Collectors.groupingBy(
                                                TimetableOfClasses::getDiscipline,
                                                Collectors.groupingBy(
                                                        TimetableOfClasses::getGroup,
                                                        Collectors.groupingBy(
                                                                TimetableOfClasses::getSubgroup,
                                                                Collectors.groupingBy(
                                                                        TimetableOfClasses::getTypeOfWork,
                                                                        Collectors.counting()))))));
        Map<Teacher, Map<Group, Map<TypeOfAddWork, Double>>> teacherAddWork = addWorkOfTeacherService.findAllByDateIsBetween(begin, end)
                .stream()
                .collect(Collectors.groupingBy(AddWorkOfTeacher::getTeacher,
                        Collectors.groupingBy(AddWorkOfTeacher::getGroup,
                                Collectors.groupingBy(AddWorkOfTeacher::getTypeOfAddWork,
                                        Collectors.reducing(0.0, AddWorkOfTeacher::getHour, Double::sum)))));

        List<Map<String, Object>> result = builderResultAddWork(teacherAddWork, builderResultTimetable(teacherWork, end));
        return new JRBeanCollectionDataSource(result);
    }

    @Override
    public JRDataSource create(LocalDate begin, LocalDate end, Teacher teacher) {
        Map<Teacher, Map<Discipline, Map<Group, Map<Integer, Map<TypeOfWork, Long>>>>> teacherWork = timetableOfClassesService.findAllByClassDateIsBetweenAndTeacherAndStatusIsTrue(begin, end, teacher)
                .stream()
                .collect(
                        Collectors.groupingBy(
                                TimetableOfClasses::getTeacher,
                                Collectors.groupingBy(
                                        TimetableOfClasses::getDiscipline,
                                        Collectors.groupingBy(
                                                TimetableOfClasses::getGroup,
                                                Collectors.groupingBy(
                                                        TimetableOfClasses::getSubgroup,
                                                        Collectors.groupingBy(
                                                                TimetableOfClasses::getTypeOfWork,
                                                                Collectors.counting()

                                                        ))))));
        Map<Teacher, Map<Group, Map<TypeOfAddWork, Double>>> teacherAddWork = addWorkOfTeacherService.findAllByDateIsBetweenAndTeacher(begin, end, teacher)
                .stream()
                .collect(Collectors.groupingBy(AddWorkOfTeacher::getTeacher,
                        Collectors.groupingBy(AddWorkOfTeacher::getGroup,
                                Collectors.groupingBy(AddWorkOfTeacher::getTypeOfAddWork,
                                        Collectors.reducing(0.0, AddWorkOfTeacher::getHour, Double::sum))
                        )));

        List<Map<String, Object>> result = builderResultAddWork(teacherAddWork, builderResultTimetable(teacherWork, end));
        return new JRBeanCollectionDataSource(result);
    }

    @Override
    public JRDataSource create(LocalDate begin, LocalDate end, Group group) {
        Map<Teacher, Map<Discipline, Map<Group, Map<Integer, Map<TypeOfWork, Long>>>>> teacherWork =
                timetableOfClassesService.findAllByClassDateIsBetweenAndGroupAndStatusIsTrue(begin, end, group)
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        TimetableOfClasses::getTeacher,
                                        Collectors.groupingBy(
                                                TimetableOfClasses::getDiscipline,
                                                Collectors.groupingBy(
                                                        TimetableOfClasses::getGroup,
                                                        Collectors.groupingBy(
                                                                TimetableOfClasses::getSubgroup,
                                                                Collectors.groupingBy(
                                                                        TimetableOfClasses::getTypeOfWork,
                                                                        Collectors.counting()))))));
        Map<Teacher, Map<Group, Map<TypeOfAddWork, Double>>> teacherAddWork = addWorkOfTeacherService.findAllByDateIsBetweenAndGroup(begin, end, group)
                .stream()
                .collect(Collectors.groupingBy(AddWorkOfTeacher::getTeacher,
                        Collectors.groupingBy(AddWorkOfTeacher::getGroup,
                                Collectors.groupingBy(AddWorkOfTeacher::getTypeOfAddWork,
                                        Collectors.reducing(0.0, AddWorkOfTeacher::getHour, Double::sum)))));

        List<Map<String, Object>> result = builderResultAddWork(teacherAddWork, builderResultTimetable(teacherWork, end));
        return new JRBeanCollectionDataSource(result);
    }

    @Override
    public JRDataSource createConsolidatedByGroup(LocalDate begin, LocalDate end, Group group) {
        Map<Teacher, Map<Group, Map<Integer, Map<TypeOfWork, Long>>>> teacherWork =
                timetableOfClassesService.findAllByClassDateIsBetweenAndGroupAndStatusIsTrue(begin, end, group)
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        TimetableOfClasses::getTeacher,
                                        Collectors.groupingBy(
                                                TimetableOfClasses::getGroup,
                                                Collectors.groupingBy(
                                                        TimetableOfClasses::getSubgroup,
                                                        Collectors.groupingBy(
                                                                TimetableOfClasses::getTypeOfWork,
                                                                Collectors.counting())))));
        Map<Teacher, Map<TypeOfAddWork, Double>> teacherAddWork = addWorkOfTeacherService.findAllByDateIsBetweenAndGroup(begin, end, group)
                .stream()
                .collect(Collectors.groupingBy(AddWorkOfTeacher::getTeacher,
                        Collectors.groupingBy(AddWorkOfTeacher::getTypeOfAddWork,
                                Collectors.reducing(0.0, AddWorkOfTeacher::getHour, Double::sum))));

        List<Map<String, Object>> result = builderResultConsolidatedByGroup(teacherWork, teacherAddWork, end);

        return new JRBeanCollectionDataSource(result);
    }

    private List<Map<String, Object>> builderResultConsolidatedByGroup(Map<Teacher, Map<Group, Map<Integer, Map<TypeOfWork, Long>>>> teacherWork,
                                                                       Map<Teacher, Map<TypeOfAddWork, Double>> teacherAddWork,
                                                                       LocalDate end) {
        List<Map<String, Object>> result = new ArrayList<>();
        teacherWork
                .forEach((key1, value1) -> {
                    Map<TypeOfWork, Double> type = new HashMap<>();
                    value1
                            .forEach((group, subgroupMapMap) -> {
                                subgroupMapMap
                                        .forEach((subgroup, works) -> {
                                            works.forEach((key, value) -> {
                                                if (!type.containsKey(key)) {
                                                    if (key == TypeOfWork.EXAM || key == TypeOfWork.TEST) {
                                                        Optional<NumberOfPersons> quantity = groupService.findNumberOfPersons(group, subgroup, end);
                                                        if (quantity.isPresent()) {
                                                            type.put(key, quantity.get().getQuantity() * TARIFF);
                                                        } else {
                                                            type.put(key, 0.0);
                                                        }
                                                    } else {
                                                        type.put(key, value.doubleValue());
                                                    }

                                                } else {
                                                    if (key == TypeOfWork.EXAM || key == TypeOfWork.TEST) {
                                                        Optional<NumberOfPersons> quantity = groupService.findNumberOfPersons(group, subgroup, end);
                                                        if (quantity.isPresent()) {
                                                            type.put(key, type.get(key) + quantity.get().getQuantity() * TARIFF);
                                                        } else {
                                                            type.put(key, type.get(key) + 0.0);
                                                        }
                                                    } else {
                                                        type.put(key, value.doubleValue() + type.get(key));
                                                    }
                                                }
                                            });

                                        });
                            });
                    Map<String, Object> item = new HashMap<>();
                    item.put("teacher", key1.getLastName() +
                            " " + key1.getFirstName() + " " +
                            key1.getPatronymic());
                    Double sum1 = type.values().stream().reduce(0.0, Double::sum);

                    Double sum2 = 0.0;
                    if (teacherAddWork.containsKey(key1)) {
                        sum2 = teacherAddWork.get(key1).values().stream().reduce(0.0, Double::sum);
                        teacherAddWork.remove(key1);
                    }
                    item.put("hours", sum1 + sum2);
                    result.add(item);
                });
        if (!teacherAddWork.isEmpty()) {
            teacherAddWork
                    .forEach((key, value) -> {
                        Map<String, Object> item = new HashMap<>();
                        Double sum = value.values().stream().reduce(0.0, Double::sum);
                        item.put("teacher", key.getLastName() +
                                " " + key.getFirstName().substring(0, 1) + "." +
                                key.getPatronymic().substring(0, 1) + ".");
                        item.put("hour", sum);
                        result.add(item);
                    });
        }
        return result;
    }


    private List<Map<String, Object>> builderResultAddWork(Map<Teacher, Map<Group, Map<TypeOfAddWork, Double>>> teacherAddWork,
                                                           List<Map<String, Object>> result) {
        teacherAddWork
                .forEach((teacher, groupMapMap) -> {
                    groupMapMap.keySet()
                            .stream()
                            .collect(Collectors.groupingBy(Group::getTypeOfEducation, Collectors.toSet()))
                            .forEach((education, groups) -> {
                                Set<TypeOfCourse> typeOfCourses = groups.stream().map(g -> g.getDescriptionOfPlan().getTypeOfCourse()).collect(Collectors.toSet());
                                typeOfCourses.forEach(
                                        course -> {
                                            Map<TypeOfAddWork, Double> type = new HashMap<>();
                                            Long index = groupMapMap
                                                    .entrySet()
                                                    .stream()
                                                    .filter(g -> ((g.getKey().getTypeOfEducation() == education) &&
                                                            (g.getKey().getDescriptionOfPlan().getTypeOfCourse() == course)))
                                                    .peek(g -> {
                                                        g.getValue()
                                                                .forEach((key, value) -> {
                                                                    if (!type.containsKey(key)) {
                                                                        type.put(key, value);
                                                                    } else {
                                                                        type.put(key, type.get(key) + value);
                                                                    }
                                                                });
                                                    }).count();
                                            type.forEach((key, value) -> {
                                                        Map<String, Object> item = new HashMap<>();
                                                        item.put("teacher", teacher.getLastName() +
                                                                " " + teacher.getFirstName().substring(0, 1) + "." +
                                                                teacher.getPatronymic().substring(0, 1) + ".");
                                                        switch (teacher.getTypeOfEmployment()) {
                                                            case STAFF: {
                                                                item.put("type_employee", "Штатные преподаватели");
                                                                break;
                                                            }
                                                            case EXTERNAL: {
                                                                item.put("type_employee", "Совметители внешние");
                                                                break;
                                                            }
                                                            case INTERNAL: {
                                                                item.put("type_employee", "Совметители внутренние");
                                                                break;
                                                            }
                                                            case CONTRACT: {
                                                                item.put("type_employee", "По договору подряда");
                                                                break;
                                                            }
                                                        }
                                                        item.put("discipline", key.getDescription().substring(0, 1).toUpperCase() + key.getDescription().substring(1).toLowerCase());
                                                        item.put("group", index);
                                                        item.put("education", education.getShort_description().toLowerCase());
                                                        item.put("course", course.getShort_description().toLowerCase());
                                                        TypeOfWork.orderedValues.forEach(
                                                                typeOfWork -> {
                                                                    item.put(typeOfWork.toString().toLowerCase(), 0.0);
                                                                }
                                                        );
                                                        TypeOfAddWork.orderedValues.stream()
                                                                .filter(typeOfAddWork -> !(typeOfAddWork == key))
                                                                .forEach(tw ->
                                                                        item.put(tw.toString().toLowerCase(), 0.0)
                                                                );
                                                        item.put(key.toString().toLowerCase(), value);
                                                        result.add(item);
                                                    }
                                            );

                                        });
                            });
                });
        return result;
    }


    private List<Map<String, Object>> builderResultTimetable(Map<Teacher, Map<Discipline, Map<Group, Map<Integer, Map<TypeOfWork, Long>>>>> teacherWork,
                                                             LocalDate end) {
        List<Map<String, Object>> result = new ArrayList<>();
        teacherWork
                .entrySet()
                .forEach(((t -> {
                    t.getValue()
                            .forEach((discipline, groupMapMap) -> {
                                        groupMapMap
                                                .keySet()
                                                .stream()
                                                .collect(Collectors.groupingBy(Group::getTypeOfEducation, Collectors.toSet()))
                                                .forEach((education, groups) -> {
                                                    Set<TypeOfCourse> typeOfCourses = groups
                                                            .stream()
                                                            .map(g -> g.getDescriptionOfPlan().getTypeOfCourse())
                                                            .collect(Collectors.toSet());
                                                    typeOfCourses.forEach(
                                                            course -> {
                                                                Map<TypeOfWork, Double> type = new HashMap<>();
                                                                Long index = groupMapMap
                                                                        .entrySet()
                                                                        .stream()
                                                                        .filter(g -> ((g.getKey().getTypeOfEducation() == education) &&
                                                                                (g.getKey().getDescriptionOfPlan().getTypeOfCourse() == course)))
                                                                        .peek(g -> {
                                                                            g.getValue()
                                                                                    .forEach((subgroup, works) -> {
                                                                                        works.forEach((key, value) -> {
                                                                                            if (!type.containsKey(key)) {
                                                                                                if (key == TypeOfWork.EXAM || key == TypeOfWork.TEST) {
                                                                                                    Optional<NumberOfPersons> quantity = groupService.findNumberOfPersons(g.getKey(), subgroup, end);
                                                                                                    if (quantity.isPresent()) {
                                                                                                        type.put(key, quantity.get().getQuantity() * TARIFF);
                                                                                                    } else {
                                                                                                        type.put(key, 0.0);
                                                                                                    }

                                                                                                } else {
                                                                                                    type.put(key, value.doubleValue());
                                                                                                }

                                                                                            } else {
                                                                                                if (key == TypeOfWork.EXAM || key == TypeOfWork.TEST) {
                                                                                                    Optional<NumberOfPersons> quantity = groupService.findNumberOfPersons(g.getKey(), subgroup, end);
                                                                                                    if (quantity.isPresent()) {
                                                                                                        type.put(key, type.get(key) + quantity.get().getQuantity() * TARIFF);
                                                                                                    } else {
                                                                                                        type.put(key, type.get(key) + 0.0);
                                                                                                    }

                                                                                                } else {
                                                                                                    type.put(key, value.doubleValue() + type.get(key));
                                                                                                }
                                                                                            }
                                                                                        });

                                                                                    });
                                                                        }).count();
                                                                Map<String, Object> item = new HashMap<>();
                                                                item.put("teacher", t.getKey().getLastName() +
                                                                        " " + t.getKey().getFirstName().substring(0, 1) + "." +
                                                                        t.getKey().getPatronymic().substring(0, 1) + ".");
                                                                switch (t.getKey().getTypeOfEmployment()) {
                                                                    case STAFF: {
                                                                        item.put("type_employee", "Штатные преподаватели");
                                                                        break;
                                                                    }
                                                                    case EXTERNAL: {
                                                                        item.put("type_employee", "Совметители внешние");
                                                                        break;
                                                                    }
                                                                    case INTERNAL: {
                                                                        item.put("type_employee", "Совметители внутренние");
                                                                        break;
                                                                    }
                                                                    case CONTRACT: {
                                                                        item.put("type_employee", "По договору подряда");
                                                                        break;
                                                                    }
                                                                }
                                                                item.put("discipline", discipline.getDisciplineName());
                                                                item.put("group", index);
                                                                item.put("education", education.getShort_description().toLowerCase());
                                                                item.put("course", course.getShort_description().toLowerCase());
                                                                TypeOfWork.orderedValues.forEach(
                                                                        typeOfWork -> {
                                                                            Double sum = type.entrySet()
                                                                                    .stream()
                                                                                    .filter(tw -> tw.getKey() == typeOfWork)
                                                                                    .map(Map.Entry::getValue)
                                                                                    .reduce(0.00, Double::sum);
                                                                            if (typeOfWork == TypeOfWork.EXAM || typeOfWork == TypeOfWork.TEST) {
                                                                                item.put(typeOfWork.toString().toLowerCase(), sum);
                                                                            }
                                                                            item.put(typeOfWork.toString().toLowerCase(), sum);
                                                                        }

                                                                );
                                                                TypeOfAddWork.orderedValues.forEach(
                                                                        typeOfAddWork -> {
                                                                            item.put(typeOfAddWork.toString().toLowerCase(), 0.0);
                                                                        }
                                                                );
                                                                result.add(item);
                                                            });

                                                });
                                    }
                            );
                })));
        return result;
    }

    private String getTypeEmployee(TypeOfEmployment type) {
        switch (type) {
            case STAFF:
                return "Штатные преподаватели";
            case EXTERNAL:
                return "Совметители внешние";
            case INTERNAL:
                return "Совметители внутренние";
            case CONTRACT:
                return "По договору подряда";
            default:
                return "";
        }
    }
}

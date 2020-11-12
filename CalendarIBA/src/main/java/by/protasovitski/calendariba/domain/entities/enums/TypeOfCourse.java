package by.protasovitski.calendariba.domain.entities.enums;


import by.protasovitski.calendariba.domain.entities.enums.json.TypeOfClassroomSerializer;
import by.protasovitski.calendariba.domain.entities.enums.json.TypeOfCourseSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonSerialize(using = TypeOfCourseSerializer.class)
public enum TypeOfCourse {
    RETRAINING("ПЕРЕПОДГОТОВКА","ПП"),
    STAFF_DEVELOPMENT("ПОВЫШЕНИЕ КВАЛИФИКАЦИИ","ПК"),
    TRAINING_COURSES("ОБУЧАЮЩИЕ КУРСЫ","ОК");

    private final String description;
    private final String short_description;

    TypeOfCourse(String description, String short_description) {
        this.description = description;
        this.short_description = short_description;
    }

    public String getDescription() {
        return description;
    }

    public String getShort_description() {
        return short_description;
    }

    public static List<TypeOfCourse> orderedValues = new ArrayList<>();

    static {
        orderedValues.addAll(Arrays.asList(TypeOfCourse.values()));
    }
}

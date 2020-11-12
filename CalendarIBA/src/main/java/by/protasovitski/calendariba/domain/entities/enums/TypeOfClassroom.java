package by.protasovitski.calendariba.domain.entities.enums;

import by.protasovitski.calendariba.domain.entities.enums.json.TypeOfClassroomSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonSerialize(using = TypeOfClassroomSerializer.class)
public enum TypeOfClassroom {
    LECTURE("ЛЕКЦИОННАЯ","Л"),
    LABORATORY("КОМПЬЮТЕРНАЯ","К"),
    WEBINAR("ВЕБИНАР","В");
    private final String description;
    private final String short_description;

    TypeOfClassroom(String description, String short_description) {
        this.description = description;
        this.short_description = short_description;
    }

    public String getDescription() {
        return description;
    }

    public String getShort_description() {
        return short_description;
    }

    public static List<TypeOfClassroom> orderedValues = new ArrayList<>();

    static {
        orderedValues.addAll(Arrays.asList(TypeOfClassroom.values()));
    }
}

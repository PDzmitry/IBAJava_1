package by.protasovitski.calendariba.domain.entities.enums;

import by.protasovitski.calendariba.domain.entities.enums.json.TypeOfEducationSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonSerialize(using = TypeOfEducationSerializer.class)
public enum TypeOfEducation {
    EXTRAMURAL_STUDIES("ЗАОЧНАЯ","ЗАО"),
    FULL_TIME_EDUCATION("ОЧНАЯ ДНЕВНАЯ","О/Д"),
    FULL_TIME_EVENING_EDUCATION("ОЧНАЯ ВЕЧЕРНЯЯ","О/В");

    private final String description;
    private final String short_description;

    TypeOfEducation(String description, String short_description) {
        this.description = description;
        this.short_description = short_description;
    }

    public String getDescription() {
        return description;
    }

    public String getShort_description() {
        return short_description;
    }

    public static List<TypeOfEducation> orderedValues = new ArrayList<>();

    static {
        orderedValues.addAll(Arrays.asList(TypeOfEducation.values()));
    }
}

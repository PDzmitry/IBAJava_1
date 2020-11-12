package by.protasovitski.calendariba.domain.entities.enums;

import by.protasovitski.calendariba.domain.entities.enums.json.TypeOfPositionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonSerialize(using = TypeOfPositionSerializer.class)
public enum TypeOfPosition {
    RECTOR("РЕКТОР"),
    HEAD_OF_DEPARTMENT("ЗАВЕДУЮЩИЙ КАФЕДРОЙ"),
    HEAD_OF_CENTER("НАЧАЛЬНИК ЦЕНТРА"),
    CHIEF_OF_DEPARTMENT("НАЧАЛЬНИК ОТДЕЛА");

    private final String description;

    TypeOfPosition(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static List<TypeOfPosition> orderedValues = new ArrayList<>();

    static {
        orderedValues.addAll(Arrays.asList(TypeOfPosition.values()));
    }
}

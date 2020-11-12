package by.protasovitski.calendariba.domain.entities.enums;

import by.protasovitski.calendariba.domain.entities.enums.json.RoleNameSerializer;
import by.protasovitski.calendariba.domain.entities.enums.json.StatusSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonSerialize(using = StatusSerializer.class)
public enum Status {
    ACTIVE("Активирован"),
    NOT_ACTIVE("Заблокирован"),
    DELETED("Удален");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public static List<Status> orderedValues = new ArrayList<>();

    static {
        orderedValues.addAll(Arrays.asList(Status.values()));
    }
}

package by.protasovitski.calendariba.domain.entities.enums;

import by.protasovitski.calendariba.domain.entities.enums.json.RoleNameSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@JsonSerialize(using = RoleNameSerializer.class)
public enum RoleName {
    ROLE_USER("Преподаватель"),
    ROLE_GROUP("Группа"),
    ROLE_MANAGER("Менеджер"),
    ROLE_VIEW("Наблюдатель"),
    ROLE_ADMIN("Администратор");

    private final String description;

    RoleName(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public static List<RoleName> orderedValues = new ArrayList<>();

    static {
        orderedValues.addAll(Arrays.asList(RoleName.values()));
    }
}

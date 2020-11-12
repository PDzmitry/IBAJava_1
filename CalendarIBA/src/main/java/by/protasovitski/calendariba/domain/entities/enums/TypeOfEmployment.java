package by.protasovitski.calendariba.domain.entities.enums;

import by.protasovitski.calendariba.domain.entities.enums.json.TypeOfEmploymentSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonSerialize(using = TypeOfEmploymentSerializer.class)
public enum TypeOfEmployment {
    STAFF("штатный","шт"),
    EXTERNAL("внешний совместитель","вне"),
    INTERNAL("внутренний совместитель","вну"),
    CONTRACT("по договору","дог");

    private final String description;
    private final String short_description;

    TypeOfEmployment(String description, String short_description) {
        this.description = description;
        this.short_description = short_description;
    }

    public String getDescription() {
        return description;
    }

    public String getShort_description() {
        return short_description;
    }

    public static List<TypeOfEmployment> orderedValues = new ArrayList<>();

    static {
        orderedValues.addAll(Arrays.asList(TypeOfEmployment.values()));
    }
}

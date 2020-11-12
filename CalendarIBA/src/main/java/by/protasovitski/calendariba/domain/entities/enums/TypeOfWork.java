package by.protasovitski.calendariba.domain.entities.enums;


import by.protasovitski.calendariba.domain.entities.enums.json.TypeOfWorkSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonSerialize(using = TypeOfWorkSerializer.class)
public enum TypeOfWork {
    LECTURE("ЛЕКЦИИ","Л"),
    LABORATORY("ЛАБОРАТОРНЫЕ","ЛЗ"),
    WEBINAR("ВЕБИНАРЫ","В"),
    PRACTICE("ПРАКТИЧЕСКИЕ ЗАНЯТИЯ","П"),
    ROUNDTABLE("КРУГЛЫЙ СТОЛ","КС"),
    BUSINESSGAMES("ДЕЛОВЫЕ ИГРЫ","ДИ"),
    EXAM("ЭКЗАМЕН","Э"),
    TEST("ЗАЧЕТ","З");



    private final String description;
    private final String short_description;

    TypeOfWork(String description, String short_description) {
        this.description = description;
        this.short_description = short_description;
    }

    public String getDescription() {
        return description;
    }

    public String getShort_description() {
        return short_description;
    }

    public static List<TypeOfWork> orderedValues = new ArrayList<>();

    static {
        orderedValues.addAll(Arrays.asList(TypeOfWork.values()));
    }
}

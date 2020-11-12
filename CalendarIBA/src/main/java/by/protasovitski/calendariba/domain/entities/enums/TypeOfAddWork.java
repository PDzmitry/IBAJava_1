package by.protasovitski.calendariba.domain.entities.enums;

import by.protasovitski.calendariba.domain.entities.enums.json.TypeOfAddWorkSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonSerialize(using = TypeOfAddWorkSerializer.class)
public enum TypeOfAddWork {

    CONSULTATION("КОНСУЛЬТИРОВАНИЕ ДИПЛОМНЫХ РАБОТ", "КОНСУЛЬТИРОВАНИЕ ДР"),
    PEERREVIEW("РЕЦЕНЗИРОВАНИЕ ДИПЛОМНЫХ РАБОТ", "РЕЦЕНЗИРОВАНИЕ ДР"),
    THESIS("РУКОВОДСТВО ДИПЛОМНЫМИ РАБОТАМИ", "РУКОВОДСТВО ДР"),
    OTHER ("ДРУГИЕ ВИДЫ УЧЕБНЫХ РАБОТ","ДВР"),
    SEC("УЧАСТИЕ В РАБОТЕ ГЭК","ГЭК"),
    SE("ГОСУДАРСТВЕННЫЙ ЭКЗАМЕН","ГЭ");


    private final String description;
    private final String short_description;

    TypeOfAddWork(String description, String short_description) {
        this.description = description;
        this.short_description = short_description;
    }

    public String getDescription() {
        return description;
    }

    public String getShort_description() {
        return short_description;
    }

    public static List<TypeOfAddWork> orderedValues = new ArrayList<>();

    static {
        orderedValues.addAll(Arrays.asList(TypeOfAddWork.values()));
    }
}

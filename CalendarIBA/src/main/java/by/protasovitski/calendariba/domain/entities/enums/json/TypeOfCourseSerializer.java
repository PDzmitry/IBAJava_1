package by.protasovitski.calendariba.domain.entities.enums.json;

import by.protasovitski.calendariba.domain.entities.enums.TypeOfCourse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class TypeOfCourseSerializer extends JsonSerializer<TypeOfCourse>{
    /**
     * Method that can be called to ask implementation to serialize
     * values of type this serializer handles.
     *
     * @param value       Value to serialize; can <b>not</b> be null.
     * @param gen         Generator used to output resulting Json content
     * @param serializers Provider that can be used to get serializers for
     */
    @Override
    public void serialize(TypeOfCourse value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("id");
        gen.writeString(value.name());
        gen.writeFieldName("value");
        gen.writeString(value.getDescription());
        gen.writeFieldName("short_value");
        gen.writeString(value.getShort_description());
        gen.writeEndObject();
    }
}
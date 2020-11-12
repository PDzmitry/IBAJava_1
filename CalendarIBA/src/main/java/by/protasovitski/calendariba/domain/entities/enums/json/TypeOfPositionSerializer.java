package by.protasovitski.calendariba.domain.entities.enums.json;

import by.protasovitski.calendariba.domain.entities.enums.TypeOfPosition;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class TypeOfPositionSerializer extends JsonSerializer<TypeOfPosition> {
    /**
     * Method that can be called to ask implementation to serialize
     * values of type this serializer handles.
     *
     * @param value       Value to serialize; can <b>not</b> be null.
     * @param gen         Generator used to output resulting Json content
     * @param serializers Provider that can be used to get serializers for
     */
    @Override
    public void serialize(TypeOfPosition value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("id");
        gen.writeString(value.name());
        gen.writeFieldName("value");
        gen.writeString(value.getDescription());
        gen.writeEndObject();
    }
}

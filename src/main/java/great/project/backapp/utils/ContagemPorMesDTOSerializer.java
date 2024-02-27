package great.project.backapp.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import great.project.backapp.model.dto.ContagemPorMesDTO;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class ContagemPorMesDTOSerializer extends JsonSerializer<ContagemPorMesDTO> {
    @Override
    public void serialize(ContagemPorMesDTO value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("mes", value.getMes());
        gen.writeNumberField("quantidade", value.getQuantidade());
        gen.writeEndObject();
    }
}
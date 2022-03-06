package br.com.rpizao.utils.serializers;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

	@Override
	public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        //node.get("")
		return null;
	}
}
package com.demo.admissionportal.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IntegerListDeserializer extends JsonDeserializer<List<Integer>> {
    @Override
    public List<Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        List<Integer> result = new ArrayList<>();
        JsonNode node = p.getCodec().readTree(p);
        if (node.isArray()) {
            for (JsonNode elementNode : node) {
                if (!elementNode.isInt()) {
                    throw new JsonProcessingException("Chỉ cho phép số tự nhiên") {};
                } if (elementNode.intValue() <= 0){
                    throw new JsonProcessingException("Môn học không tồn tại"){};
                }
                result.add(elementNode.asInt());
            }
        }
        return result;
    }
}

package edu.search.fileparser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import edu.search.vo.WordInFileCount;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * custom deserializer
 */
public class StatisticsDeserializer extends StdDeserializer<Map<String, Set<WordInFileCount>>> {


    public StatisticsDeserializer() {
        super(HashMap.class);
    }

    protected StatisticsDeserializer(Class<?> vc) {
        super(vc);
    }

    protected StatisticsDeserializer(JavaType valueType) {
        super(valueType);
    }

    @Override
    public Map<String, Set<WordInFileCount>> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        Map<String, Set<WordInFileCount>> data = new HashMap<>(50);
        JsonNode node = jp.getCodec().readTree(jp);
        if (node.isObject()) {
            node.fields().forEachRemaining(f -> {
                Set<WordInFileCount> wcs = new TreeSet();
                if (f.getValue().isArray()) {
                    ArrayNode childNode = (ArrayNode) f.getValue();
                    for (int i =0 ; i < childNode.size() ; i++) {
                        JsonNode wcNode = childNode.get(i);
                        wcs.add(new WordInFileCount(wcNode.get("fileName").asText(), wcNode.get("relevancy").asInt()));
                    }
                }
                data.put(f.getKey() , wcs);
            });
        }
        return data;


    }
}


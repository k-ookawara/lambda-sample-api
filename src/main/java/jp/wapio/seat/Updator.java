package jp.wapio.seat;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Updator implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        return update(null, input);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> update(Map<String, Object> source, Map<String, Object> input) {
        if (input == null) {
            if (source == null) {
                return null;
            } else {
                return clone(source);
            }
        } else {
            if (source == null) {
                return clone(input);
            }
        }
        Map<String, Object> updated = clone(source);
        for (Entry<String, Object> entry : input.entrySet()) {
            String inputKey = entry.getKey();
            Object inputValue = entry.getValue();
            Object sourceValue = updated.get(inputKey);
            if (inputValue == null || sourceValue == null) {
                updated.put(inputKey, inputValue);
            } else if (inputValue instanceof Map<?, ?> && sourceValue instanceof Map<?, ?>) {
                updated.put(inputKey, update((Map<String, Object>) sourceValue, (Map<String, Object>) inputValue));
            } else if (inputValue instanceof List<?> && sourceValue instanceof List<?>) {
                updated.put(inputKey, update((List<Object>) inputValue));
            } else if (inputValue.getClass().isInstance(sourceValue)) {
                updated.put(inputKey, inputValue);
            } else {
                throw new RuntimeException();
            }
        }
        return updated;
    }

    @SuppressWarnings("unchecked")
    public List<Object> update(List<Object> list) {
        var updated = new ArrayList<Object>();
        for (var attr : list) {
            if (attr instanceof Map<?, ?>) {
                updated.add(update(null, (Map<String, Object>) attr));
            } else if (attr instanceof List<?>) {
                updated.add(update((List<Object>) attr));
            } else {
                updated.add(attr);
            }
        }
        return updated;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> clone(Map<String, Object> source) {
        try {
            Map<String, Object> clonedMap = source.getClass().getConstructor().newInstance();
            for (Entry<String, Object> entry : source.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof Map<?, ?>) {
                    clonedMap.put(entry.getKey(), clone((Map<String, Object>) value));
                } else if (value instanceof List<?>) {
                    clonedMap.put(entry.getKey(), clone((List<Object>) value));
                } else {
                    clonedMap.put(entry.getKey(), value);
                }
            }
            return clonedMap;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Object> clone(List<Object> source) {
        try {
            List<Object> list = source.getClass().getConstructor().newInstance();
            for (Object attribute : source) {
                if (attribute instanceof Map) {
                    list.add(clone((Map<String, Object>) attribute));
                } else {
                    list.add(attribute);
                }
            }
            return list;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

}

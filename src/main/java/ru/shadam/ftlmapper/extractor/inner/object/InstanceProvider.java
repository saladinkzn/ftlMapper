package ru.shadam.ftlmapper.extractor.inner.object;

import java.util.Map;

/**
 * @author sala
 */
public interface InstanceProvider<T> {
    public T newInstance(Map<String, Object> attributes);

}

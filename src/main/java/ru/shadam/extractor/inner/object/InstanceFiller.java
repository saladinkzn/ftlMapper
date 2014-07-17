package ru.shadam.extractor.inner.object;

import java.util.Map;

/**
 * @author sala
 */
public interface InstanceFiller<T> {
    public void fillInstance(T instance, Map<String, Object> attributes);
}

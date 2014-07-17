package ru.shadam.extractor.inner.object;

import ru.shadam.extractor.inner.InnerResultSetExtractor;
import ru.shadam.extractor.state.ObjectState;
import ru.shadam.extractor.state.SimpleState;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sala
 */
public class ObjectResultSetExtractor<T> implements InnerResultSetExtractor<ObjectState<T>> {
    private final InstanceProvider<T> instanceProvider;
    private final List<? extends InstanceFiller<T>> instanceFillers;
    private final Map<String, InnerResultSetExtractor<? extends SimpleState<?>>> attributeExtractors;
    //

    public ObjectResultSetExtractor(InstanceProvider<T> instanceProvider,
                                    List<? extends InstanceFiller<T>> instanceFillers,
                                    Map<String, InnerResultSetExtractor<? extends SimpleState<?>>> attributeExtractors) {
        this.instanceProvider = instanceProvider;
        this.instanceFillers = instanceFillers;
        this.attributeExtractors = attributeExtractors;
    }

    private T calculateValue(Map<String, Object> childValues) {
        try {
            final T instance = instanceProvider.newInstance(childValues);
            for(InstanceFiller<T> instanceFiller: instanceFillers) {
                instanceFiller.fillInstance(instance, childValues);
            }
            return instance;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public  ObjectState<T> consumeRow(ObjectState<T> state, ResultSet resultSet) throws SQLException {
        final boolean completed;
        final Map<String, SimpleState<?>> childStates = new HashMap<>();
        //
        for(Map.Entry<String, InnerResultSetExtractor<? extends SimpleState<?>>> innerResultSetExtractorEntry: attributeExtractors.entrySet()) {
            final String fieldName = innerResultSetExtractorEntry.getKey();
            final InnerResultSetExtractor<SimpleState<?>> innerResultSetExtractor = (InnerResultSetExtractor<SimpleState<?>>) innerResultSetExtractorEntry.getValue();
            final SimpleState<?> simpleState = state.getChildStates().get(fieldName);
            childStates.put(fieldName, innerResultSetExtractor.consumeRow(simpleState, resultSet));
        }
        {
            boolean _completed = true;
            for(SimpleState<?> simpleState: childStates.values()) {
                if(!simpleState.isCompleted()) {
                    _completed = false;
                    break;
                }
            }
            completed = _completed;
        }
        //
        final Map<String, Object> attributeValues = new HashMap<>(state.getValues());
        for(Map.Entry<String, InnerResultSetExtractor<? extends SimpleState<?>>> innerResultSetExtractorEntry: attributeExtractors.entrySet()) {
            final String fieldName = innerResultSetExtractorEntry.getKey();
            final SimpleState<?> childSimpleState = childStates.get(fieldName);
            if (childSimpleState.isCompleted()) {
                if(!attributeValues.containsKey(fieldName)) {
                    attributeValues.put(fieldName, childSimpleState.getValue());
                }
            }
        }
        final T value;
        if(completed) {
            value = calculateValue(attributeValues);
        } else {
            value = null;
        }
        return new ObjectState<>(completed, value, attributeValues, childStates);
    }

    @Override
    public ObjectState<T> complete(ObjectState<T> objectState) {
        if(objectState.isCompleted()) {
            return objectState;
        }
        final Map<String, SimpleState<?>> childStates = objectState.getChildStates();
        //
        final Map<String, Object> attributeValues = new HashMap<>(objectState.getValues());
        for(Map.Entry<String, InnerResultSetExtractor<? extends SimpleState<?>>> innerResultSetExtractorEntry: attributeExtractors.entrySet()) {
            final String fieldName = innerResultSetExtractorEntry.getKey();
            final InnerResultSetExtractor<SimpleState<?>> innerResultSetExtractor = (InnerResultSetExtractor<SimpleState<?>>) innerResultSetExtractorEntry.getValue();
            final SimpleState<?> newChildState;
            {
                final SimpleState<?> childState = childStates.get(fieldName);
                newChildState = innerResultSetExtractor.complete(childState);
            }
            if(!attributeValues.containsKey(fieldName)) {
                final Object childValue = newChildState.getValue();
                attributeValues.put(fieldName, childValue);
            }
        }
        //
        T value = calculateValue(attributeValues);
        // TODO: new ObjectState<>(true, value, null, null)
        return new ObjectState<>(true, value, attributeValues, childStates);
    }

    @Override
    public ObjectState<T> getNewState(ObjectState<T> oldState) {
        final Map<String, SimpleState<?>> newChildStates = new HashMap<>();
        for(Map.Entry<String, InnerResultSetExtractor<? extends SimpleState<?>>> entry: attributeExtractors.entrySet()) {
            final String key = entry.getKey();
            final InnerResultSetExtractor<SimpleState<?>> innerResultSetExtractor = (InnerResultSetExtractor<SimpleState<?>>) entry.getValue();
            if(oldState != null) {
                newChildStates.put(key, innerResultSetExtractor.getNewState(oldState.getChildStates().get(key)));
            } else {
                newChildStates.put(key, innerResultSetExtractor.getNewState(null));
            }
        }
        return new ObjectState<>(newChildStates);
    }


}

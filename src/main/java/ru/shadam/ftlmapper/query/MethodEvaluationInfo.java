package ru.shadam.ftlmapper.query;

import ru.shadam.ftlmapper.ast.ASTParser;
import ru.shadam.ftlmapper.extractor.ResultSetExtractor;
import ru.shadam.ftlmapper.extractor.ResultSetExtractorFactory;
import ru.shadam.ftlmapper.annotations.query.Param;
import ru.shadam.ftlmapper.annotations.query.Query;
import ru.shadam.ftlmapper.annotations.query.Template;
import ru.shadam.ftlmapper.query.query.RawQueryStrategy;
import ru.shadam.ftlmapper.query.query.TemplateQueryStrategy;
import ru.shadam.ftlmapper.util.QueryManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Timur Shakurov
 */
public class MethodEvaluationInfo {
    private final ResultSetExtractor resultSetExtractor;
    private final QueryStrategy queryStrategy;
    //

    public MethodEvaluationInfo(QueryManager queryManager, Method method, ResultSetExtractorFactory resultSetExtractorFactory, ASTParser astParser) {

        final Template template = method.getAnnotation(Template.class);
        if(template != null) {
            final Annotation[][] annotations = method.getParameterAnnotations();
            final Map<Integer, String> map = new HashMap<>();
            for(int index = 0; index < annotations.length; index++) {
                for(Annotation annotation: annotations[index]) {
                    if(annotation instanceof Param) {
                        final String name = ((Param) annotation).value();
                        map.put(index, name);
                    }
                }
            }
            this.queryStrategy = new TemplateQueryStrategy(queryManager, template.value(), map);
        } else {
            final Query query = method.getAnnotation(Query.class);
            if(query != null) {
                this.queryStrategy = new RawQueryStrategy(query.value());
            } else {
                throw new IllegalArgumentException("Either @Query or @Template annotation must be specified");
            }
        }
        //
        resultSetExtractor = resultSetExtractorFactory.generate(astParser.parse("", "", method.getGenericReturnType()));
    }

    public QueryStrategy getQueryStrategy() {
        return queryStrategy;
    }

    public ResultSetExtractor<?> getResultSetExtractor() {
        return resultSetExtractor;
    }
}

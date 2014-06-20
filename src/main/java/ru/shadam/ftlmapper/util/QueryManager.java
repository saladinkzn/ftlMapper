package ru.shadam.ftlmapper.util;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * Looks for templates and rendering sqls
 *
 * @author Timur Shakurov
 */
public class QueryManager {
    private Logger logger = LoggerFactory.getLogger(QueryManager.class);

    private Configuration configuration;

    private static final Configuration DEFAULT;

    static {
        DEFAULT = new Configuration();
        DEFAULT.setClassForTemplateLoading(QueryManager.class, "/");
        DEFAULT.setNumberFormat("0.######");
        DEFAULT.setLocalizedLookup(false);
        DEFAULT.setDefaultEncoding("UTF-8");
        //
        final BeansWrapper wrapper = new BeansWrapper();
        wrapper.setSimpleMapWrapper(true);
        DEFAULT.setObjectWrapper(wrapper);

    }

    public QueryManager() {
        this(DEFAULT);
    }

    public QueryManager(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getQuery(String templateName, Map<String, ?> params) {
        // TODO: do we need BufferedWriter on top of StringWriter?
        try(final StringWriter stringWriter = new StringWriter();
            final BufferedWriter bufferedWriter = new BufferedWriter(stringWriter)
        ) {
            final Template template = configuration.getTemplate(templateName);
            template.process(params, bufferedWriter);
            bufferedWriter.flush();
            return stringWriter.toString();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}

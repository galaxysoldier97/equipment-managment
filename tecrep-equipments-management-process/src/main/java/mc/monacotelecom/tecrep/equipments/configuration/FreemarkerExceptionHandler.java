package mc.monacotelecom.tecrep.equipments.configuration;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.Writer;

public class FreemarkerExceptionHandler implements TemplateExceptionHandler {
    @Override
    public void handleTemplateException(TemplateException te, Environment env, Writer out) throws TemplateException {
        // Add exception handling here if needed
        throw te;
    }
}

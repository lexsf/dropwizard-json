package com.yammer.dropwizard.jetty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.yammer.dropwizard.json.ObjectMapperFactory;
import com.yammer.dropwizard.logging.Log;
import com.yammer.dropwizard.validation.InvalidEntityException;
import org.eclipse.jetty.server.handler.ErrorHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Lexi
 * Since: version 0.1
 */
public class JSONErrorHandler extends ErrorHandler {
    public JSONErrorHandler() {
        super();
        setShowStacks(false);
    }

    @Override
    public void writeErrorPage(HttpServletRequest request, Writer writer, int code, String message, boolean showStacks) throws IOException {
        ObjectMapper mapper = new ObjectMapperFactory().build();
        Map<String, String> map = new HashMap<String, String>();
        map.put("errorCode", Integer.toString(code));
        map.put("errorMessage", message);
        mapper.writeValue(writer, map);
    }

    @Override
    protected void writeErrorPageBody(HttpServletRequest request, Writer writer, int code, String message, boolean showStacks) throws IOException {
        ObjectMapper mapper = new ObjectMapperFactory().build();
        Map<String, String> map = new HashMap<String, String>();
        map.put("errorCode", Integer.toString(code));
        map.put("errorMessage", message);
        mapper.writeValue(writer, map);
    }

    public void writeValidationErrorPage(HttpServletRequest request, StringWriter writer, InvalidEntityException exception) throws IOException {
        ObjectMapper mapper = new ObjectMapperFactory().build();
        Map<String, String> map = new HashMap<String, String>();
        map.put("errorMessage", exception.getMessage());
        map.put("errorCode", "422");

        StringBuilder errorList = new StringBuilder();
        ImmutableList<String> errors = exception.getErrors();
        if (errors != null) {
            for (String err : errors) {
                errorList.append(err);
            }
        }
        map.put("errorArray", errorList.toString());
        mapper.writeValue(writer, map);
    }
}

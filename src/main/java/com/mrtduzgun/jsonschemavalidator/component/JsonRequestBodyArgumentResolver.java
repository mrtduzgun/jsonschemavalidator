package com.mrtduzgun.jsonschemavalidator.component;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonRequestBodyArgumentResolver implements HandlerMethodArgumentResolver {

    private RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor;

    /**
     * Construct this argument resolver, with the corresponding delegate for reading
     * payload values as if they were annotated with @{@link RequestBody}.
     */
    public JsonRequestBodyArgumentResolver(RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor) {
        this.requestResponseBodyMethodProcessor = requestResponseBodyMethodProcessor;
    }

    private Resource resolveJsonSchemaResource(MethodParameter methodParameter) {

        JsonSchemaValidate annotation = methodParameter.getParameterAnnotation(JsonSchemaValidate.class);
        String schemaPath = annotation.schemaPath();

        if (!schemaPath.isEmpty()) {
            return new ClassPathResource("schema/" + schemaPath + ".json");

        } else {
            return new ClassPathResource("schema/" + methodParameter.getParameterType().getSimpleName().toLowerCase() + ".json");
        }
    }

    private void validate(MethodParameter parameter, NativeWebRequest webRequest) throws JsonSchemaException {

        try {
            HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
            String requestBodyJson = StreamUtils.copyToString(httpServletRequest.getInputStream(),
                    StandardCharsets.UTF_8);

            JSONObject srcSchemaFileJson = new JSONObject(
                    new JSONTokener(resolveJsonSchemaResource(parameter).getInputStream()));
            SchemaLoader
                    .load(srcSchemaFileJson)
                    .validate(new JSONObject(requestBodyJson));

        } catch (ValidationException e) {
            throw new JsonSchemaValidationException(e.getErrorMessage(), e.toJSON());

        } catch (IOException e) {
            throw new UnavailableJsonSchemaException(e.getMessage(), e);
        }
    }
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonSchemaValidate.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        validate(parameter, webRequest);
        return this.requestResponseBodyMethodProcessor.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
    }
}

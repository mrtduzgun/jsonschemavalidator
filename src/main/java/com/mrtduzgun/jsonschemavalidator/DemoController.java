package com.mrtduzgun.jsonschemavalidator;

import com.mrtduzgun.jsonschemavalidator.component.JsonSchemaValidate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/json-schema-validator-spring")
public class DemoController {

    @RequestMapping("/create-sample")
    public void createSampleObject(@JsonSchemaValidate SampleObject sampleObject) {

        // Your logic goes here
    }
}

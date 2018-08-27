package com.mrtduzgun.jsonschemavalidator.component;

import org.json.JSONObject;

public class JsonSchemaValidationException extends JsonSchemaException {

    private JSONObject messageAsJson;

    public JsonSchemaValidationException(String msg, JSONObject messageAsJson) {
        super(msg);
        this.messageAsJson = messageAsJson;
    }

    public JSONObject getMessageAsJson() {
        return messageAsJson;
    }
}

package com.shaan.api.myapp.utils.objectmappers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    static final Logger logger = LoggerFactory.getLogger(LocalDateTimeSerializer.class);

    @Override
    public void serialize(LocalDateTime dateTime, JsonGenerator jg,
                          SerializerProvider sp) throws IOException, JsonProcessingException {

        logger.debug("###Serializing DateTime " + dateTime);
        Instant instant = dateTime.toInstant(ZoneOffset.UTC);
        jg.writeString(DateTimeFormatter.ISO_INSTANT.format(instant));

    }

}

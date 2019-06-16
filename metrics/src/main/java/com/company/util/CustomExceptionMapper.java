package com.company.util;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.LoggerConfig;
import com.google.common.flogger.StackSize;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<Exception> {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public CustomExceptionMapper() {
        LoggerConfig.of(logger).addHandler(LogFile.getLogFile());
    }

    @Override
    public Response toResponse(Exception exception) {
        logger.atWarning()
                .withStackTrace(StackSize.FULL)
                .log("An expected error occurred: %s", exception.getMessage());

        return Response.status(500)
                .entity(exception.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}

package ru.kusoft.library.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Data
@ConfigurationProperties("application")
public class ApplicationSettings {
    private static final InputStream INPUT_STREAM = System.in;
    private static final PrintStream PRINT_STREAM = System.out;

    public InputStream getInputStream() {
        return INPUT_STREAM;
    }

    public PrintStream getPrintStream() {
        return PRINT_STREAM;
    }
}

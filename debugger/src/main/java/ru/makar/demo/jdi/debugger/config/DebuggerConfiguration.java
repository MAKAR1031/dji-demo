package ru.makar.demo.jdi.debugger.config;

import java.io.IOException;
import java.util.Properties;

public class DebuggerConfiguration {
    private static final String FILE_NAME = "configuration.properties";
    private final Properties properties = new Properties();

    public void load() throws IOException {
        properties.clear();
        properties.load(getClass().getClassLoader().getResourceAsStream(FILE_NAME));
    }

    public String getTransport() {
        return (String) properties.get("debug.transport");
    }

    public String getHost() {
        return (String) properties.get("debug.host");
    }

    public String getPort() {
        return (String) properties.get("debug.port");
    }

    public String getVariableName() {
        return (String) properties.get("variable.name");
    }

    public String getNewValue() {
        return (String) properties.get("variable.new.value");
    }
}

package io.pivotal.pcfmetrics.examples.trace.systemtests;

import org.springframework.util.SocketUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {

    private final int port;
    private final Process process;
    private File logFile;

    public App(String appName, Map<String, String> env) {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", appName + "-trace-example-0.0.1-SNAPSHOT.jar");
        Map<String, String> environment = processBuilder.environment();

        int port = SocketUtils.findAvailableTcpPort();
        environment.put("SERVER_PORT", Integer.toString(port));
        environment.putAll(env);
        processBuilder.directory(new File("../applications/" + appName + "/build/libs/"));

        File logFile = new File("tmp/" + appName + ".log");
        processBuilder.redirectOutput(logFile);

        this.port = port;
        try {
            this.process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException("app " + appName + " failed to start", e);
        }
        this.logFile = logFile;
    }

    public void destroy() {
        process.destroy();
    }

    public int port() {
        return port;
    }

    public Trace getTrace() throws IOException {
        String logs = new String(Files.readAllBytes(Paths.get(logFile.getAbsolutePath())));
        Matcher m = Pattern.compile("\\[[^,]*,([a-f0-9]+),([a-f0-9]+),([a-f0-9]+),?\\w*\\]").matcher(logs);
        m.find();

        return new Trace(m.group(1), m.group(2), m.group(3));
    }
}

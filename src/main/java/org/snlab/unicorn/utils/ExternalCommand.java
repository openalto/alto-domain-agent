package org.snlab.unicorn.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalCommand {
    private final static Logger LOG = LoggerFactory.getLogger(ExternalCommand.class);

    public static String callExternalCommand(String program, String defaultOutput, String... args) {
        Runtime runtime = Runtime.getRuntime();
        ArrayList<String> cmd = new ArrayList<>();
        cmd.add(program);
        cmd.addAll(Arrays.asList(args));
        String[] command = cmd.toArray(new String[cmd.size()]);
        String resultMsg = "";
        String errorMsg = "";
        String line;

        try {
            LOG.info(Arrays.toString(command));
            Process process = runtime.exec(command);
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = stdout.readLine()) != null) {
                resultMsg += line;
            }
            while ((line = stderr.readLine()) != null) {
                errorMsg += line;
            }
            stderr.close();
            stdout.close();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            LOG.error("Error occurs when executing " + program, e);
            resultMsg = defaultOutput;
        }
        if (!errorMsg.isEmpty()) {
            LOG.error(errorMsg);
            resultMsg = defaultOutput;
        }
        return resultMsg;
    }
}
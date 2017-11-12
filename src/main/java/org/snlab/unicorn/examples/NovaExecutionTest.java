package org.snlab.unicorn.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NovaExecutionTest {
    // private final static Logger LOG = LoggerFactory.getLogger(NovaExecutionTest.class);

    private static String callNovaForRSA(String response) {
        Runtime runtime = Runtime.getRuntime();
        String command = "nova '" + response + "'";
        String resultMsg = "";
        String errorMsg = "";
        String line;
        try {
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
            e.printStackTrace();
            resultMsg = response;
        }
        if (!errorMsg.isEmpty()) {
            System.out.println(errorMsg);
            resultMsg = response;
        }
        return resultMsg;
    }

    public static void main(String[] args) {
        String testData = "{\"anes\": [{\"availbw\": 3}, {\"availbw\": 7}, {\"availbw\": 3}], \"ane-matrix\": [[{\"flow-id\": \"0\"}, {\"flow-id\": \"1\"}], [{\"flow-id\": \"1\"}, {\"flow-id\": \"2\"}], [{\"flow-id\": \"2\"}]]}";
        String newData = callNovaForRSA(testData);
        System.out.println(testData);
        System.out.println(newData);
    }
}
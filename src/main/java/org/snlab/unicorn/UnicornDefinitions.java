package org.snlab.unicorn;

public class UnicornDefinitions {
    public static class OrchestratorConfig {
        public static String CONFIG_PATH = "/orchestrator/orchestrators.json";
    }

    public static String SERVER_NAME = "DOMAIN_1";
    public static String SERVER_IP = "10.0.0.1";
    public static String SERVER_PORT = "8181";
    public static String CONTROL_URL = "http://" + SERVER_IP + ":" + SERVER_PORT + "/experimental/v1/control";
}

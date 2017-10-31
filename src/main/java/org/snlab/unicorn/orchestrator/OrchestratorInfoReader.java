package org.snlab.unicorn.orchestrator;

import org.snlab.unicorn.UnicornDefinitions;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrchestratorInfoReader {
    public static Collection<OrchestratorInfo> raad(String orchestratorConfigPath) {
        List<OrchestratorInfo> infos = new ArrayList<>();
        InputStream stream = OrchestratorInfo.class.getClassLoader().getResourceAsStream(orchestratorConfigPath);

        JsonReader rdr = Json.createReader(stream);
        JsonArray orchestrators = rdr.readArray();
        for(JsonObject orchestrator: orchestrators.getValuesAs(JsonObject.class)){
            OrchestratorInfo info = new OrchestratorInfo(
                    orchestrator.getInt("id"),
                    orchestrator.getString("ip"),
                    orchestrator.getString("registry")
            );
            infos.add(info);
        }
        return infos;
    }
}

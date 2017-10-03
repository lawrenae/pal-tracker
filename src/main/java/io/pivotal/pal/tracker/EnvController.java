package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class EnvController {

    public static final String PORT = "PORT";
    public static final String MEMORY_LIMIT = "MEMORY_LIMIT";
    public static final String CF_INSTANCE_INDEX = "CF_INSTANCE_INDEX";
    public static final String CF_INSTANCE_ADDR = "CF_INSTANCE_ADDR";

    private final Map<String, String> ENV;

    public EnvController(@Value("${PORT:NOT SET}") String port,
                         @Value("${MEMORY_LIMIT:NOT SET}") String memoryLimit,
                         @Value("${CF_INSTANCE_INDEX:NOT_SET}") String cfInstanceIndex,
                         @Value("${CF_INSTANCE_ADDR:NOT_SET}") String cfInstanceAddr) {
        ENV = new LinkedHashMap<>();
        ENV.put(PORT, port);
        ENV.put(MEMORY_LIMIT, memoryLimit);
        ENV.put(CF_INSTANCE_INDEX, cfInstanceIndex);
        ENV.put(CF_INSTANCE_ADDR, cfInstanceAddr);
    }

    @GetMapping("/env")
    public Map<String, String> getEnv() {
        return ENV;
    }
}

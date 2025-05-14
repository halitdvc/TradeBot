package Utils;

import java.io.*;
import java.util.*;

public class EnvReader {
    private final Map<String, String> envMap = new HashMap<>();

    public EnvReader() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(".env"));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty() || line.startsWith("#")) continue;
            String[] parts = line.split("=", 2);
            if (parts.length == 2) {
                envMap.put(parts[0].trim(), parts[1].trim());
            }
        }
        reader.close();
    }

    public String get(String key) {
        return envMap.get(key);
    }
}
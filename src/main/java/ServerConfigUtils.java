import java.util.HashMap;

public final class ServerConfigUtils {
    static HashMap<String, Object> getRunArguments(String[] args) {
        HashMap<String, Object> executionParameters = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--")) {
                String key = args[i].substring(2);
                if ( i+1 < args.length - 1 && !args[i+1].startsWith("--")) {
                    executionParameters.put(key, args[i+1]);
                    ++i;
                }
                else {
                    executionParameters.put(key, true);
                }
            }
        }
        return executionParameters;
    }
}

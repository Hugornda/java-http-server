package server;


import java.util.HashMap;

public record Route(String method, String path) {

    public HashMap<String, String> getPathVariables( String requestPath) {
        HashMap<String, String> pathVariables = new HashMap<>();
        String[] pathParts = path.split("/");
        String[] reqPathParts = requestPath.split("/");

        for (int i = 0; i < reqPathParts.length; i++) {
            if( !pathParts[i].startsWith("{") || !pathParts[i].endsWith("}") ) {
                continue;
            }
            String variableName = pathParts[i].substring(1, reqPathParts[i].length() - 1);
            String variableValue = reqPathParts[i];
            pathVariables.put(variableName, variableValue);
        }

        return pathVariables;
    }
}

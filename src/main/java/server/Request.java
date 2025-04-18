package server;

import java.util.HashMap;

public class Request {

  private final String method;

  private final String path;

  HashMap<String, String> pathVariables = new HashMap<>();

  public Request(String method, String path) {
    this.method = method;
    this.path = path;
  }

  public String getMethod() {
    return method;
  }

  public String getPath() {
    return path;
  }

  public void putPathVariable(String key, String val) {
    this.pathVariables.put(key, val);
  }

  public HashMap<String, String> getPathVariables() {
    return this.pathVariables;
  }

}

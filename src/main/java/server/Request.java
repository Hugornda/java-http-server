package server;

public class Request {

  String method;

  String path;

  public Request(String method, String path) {
    this.method = method;
    this.path = path;
  }

  public String getMethod() {
    return this.method;
  }

  public String getPath() {
    return this.path;
  }

}

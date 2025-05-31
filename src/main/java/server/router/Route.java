package server.router;


import server.model.HttpMethod;

public record Route(HttpMethod method, String path) {}

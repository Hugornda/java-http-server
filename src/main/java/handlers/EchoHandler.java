package handlers;

import server.handlers.HandlerFunction;
import server.model.Request;
import server.model.Response;

public class EchoHandler implements HandlerFunction{
    @Override
    public Response apply(Request request) {
        return new Response(200, "OK", request.getPathVariables().get("str"));
    }
}

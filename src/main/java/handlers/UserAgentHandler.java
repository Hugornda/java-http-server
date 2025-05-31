package handlers;

import server.handlers.HandlerFunction;
import server.model.Request;
import server.model.Response;

import java.util.logging.Handler;

public class UserAgentHandler implements HandlerFunction{
    @Override
    public Response apply(Request request) {
        return new Response(200,"OK", request.getHeaders().get("User-Agent"));
    }
}

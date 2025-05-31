package server.handlers;

import server.model.Request;
import server.model.Response;

public class NotFoundHandlerFunction implements HandlerFunction {
    @Override
    public Response apply(Request request) {
        return new Response(404, "Not Found");
    }
}

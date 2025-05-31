package server.handlers;

import server.model.Request;
import server.model.Response;

import java.util.function.Function;

public interface HandlerFunction extends Function<Request, Response> {
}

package server.router;

import server.handlers.HandlerFunction;
import server.model.HttpMethod;
import server.model.Request;
import server.model.Response;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

public class Router {
    private final HashMap<Route, Function<Request, Response>> routes;
    private Function<Request,Response> notFound;

    public Router() {
        routes = new HashMap<>();
    }

    public void addRoute(Route route, HandlerFunction requestHandler) {
        routes.put(route, requestHandler);
    }

    public void registerRouteNotFound(Function<Request, Response> o) {
        notFound = o;
    }

    public Response handle(Request request) {
        Route route = getRouteAndSetPathVariables(request.getMethod(), request);

        if (route != null) {

            return routes.get(route).apply(request);
        }

        return notFound.apply(request);
    }


    private Route getRouteAndSetPathVariables(HttpMethod reqMethod, Request request) {
        String[] reqPathElems = request.getPath().split("/");

        for (Route route : routes.keySet()) {
            boolean found = true;

            if (!Objects.equals(reqMethod, route.method())){
                continue;
            }

            String[] routePathElems = route.path().split("/");

            if (reqPathElems.length != routePathElems.length) {
                continue;
            }

            for (int i = 0; i < routePathElems.length ; i++) {
                if(routePathElems[i].startsWith("{") && routePathElems[i].endsWith("}")) {
                    String pathVariableName = routePathElems[i].substring(1, routePathElems[i].length() - 1);
                    request.putPathVariable(pathVariableName, reqPathElems[i]);
                    continue;
                }

                if (routePathElems[i].equals(reqPathElems[i])) {
                    continue;
                }

                found = false;
                break;
            }

            if(found) {
                return route;
            }
        }

        return null;
    }
}

package handlers;

import server.handlers.HandlerFunction;
import server.model.ContentType;
import server.model.Request;
import server.model.Response;

import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FilesHandler implements HandlerFunction {

    private static final Logger logger = Logger.getLogger(FilesHandler.class.getName());
    private final String resourcePath;

    public FilesHandler(String resourcePath) {
        this.resourcePath = resourcePath;
    }
    @Override
    public Response apply(Request request) {
        String fileName = request.getPathVariables().get("fileName");
        String content = null;
        Path dir = Paths.get(resourcePath + fileName);

        try {
            content = Files.readString(dir);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to read file: " + fileName, e);
            return new Response(404, "Not Found", content);
        }

        Response response = new Response(200, "OK", content);

        response.setContentType(ContentType.OCTET_STREAM);

        return response;
    }
}

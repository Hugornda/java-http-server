package handlers;

import server.handlers.HandlerFunction;
import server.model.Request;
import server.model.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileCreatorHandler implements HandlerFunction {
    private static final Logger logger = Logger.getLogger(FileCreatorHandler.class.getName());
    private final String resourcePath;

    public FileCreatorHandler(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public Response apply(Request request) {
        String fileName = request.getPathVariables().get("fileName");

        Path dir = Paths.get(resourcePath + fileName);

        try {
             Files.write(dir, request.getBody().getBytes());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to write file: " + fileName, e);
            return new Response(404, "Not Found");
        }

        return  new Response(201, "Created");


    }
}

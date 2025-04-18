package server;

public class Response {

    int status;

    String message;

    String body;

    public Response(int status, String message){
        this.status = status;
        this.message = message;
    }

    public Response(int status, String message, String body){
        this(status, message);
        this.body = body;
    }

    @Override
    public String toString() {
        String responseContentLength = body == null? "" : ("\r\nContent-Length: " + body.length() );
        String responseBody = body == null? "" : ("\r\n\r\n" + body);
        String responseContentType =  body == null? "" : "\r\nContent-Type: text/plain" ;
        return "HTTP/1.1 " + status + " " + message +
                responseContentType+
                responseContentLength +
                responseBody +
                "\r\n\r\n";
    }
}

package server.model;

public class Response {

    int status;

    String message;

    String body;

    String contentType = "text/plain";

    public Response(int status, String message){
        this.status = status;
        this.message = message;
    }

    public Response(int status, String message, String body){
        this(status, message);
        this.body = body;
    }

    public void setContentType(String contentType){
        this.contentType =  contentType;
    }

    public String getContentType(){
        if(contentType == null){
            return "";
        }

        return contentType;
    }

    public String getResponseContentLength(){
        if(body == null){
            return "";
        }

        return "" + body.length();
    }

    @Override
    public String toString() {
        String responseBody = body == null? "" : ("\r\n\r\n" + body);

        return "HTTP/1.1 " + status + " " + message +
                "\r\nContent-Type: " + getContentType() +
                "\r\nContent-Length: " + getResponseContentLength() +
                responseBody +
                "\r\n\r\n";
    }
}

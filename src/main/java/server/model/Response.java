package server.model;

import java.util.Locale;

public class Response {

    int status;

    String message;

    String body;

    ContentType contentType = ContentType.TEXT_PLAIN;

    ContentEncoding encoding;


    public Response(int status, String message){
        this.status = status;
        this.message = message;
    }

    public Response(int status, String message, String body){
        this(status, message);
        this.body = body;
    }

    public void setContentType(ContentType contentType){
        this.contentType =  contentType;
    }

    public ContentType getContentType(){
        if(contentType == null){
            return ContentType.NO_CONTENT_TYPE;
        }

        return contentType;
    }

    public int getResponseContentLength(){
        if(body == null){
            return 0;
        }

        return body.length();
    }

    public String getBody(){
        return body;
    }

    public void setBody(String body){
        this.body = body;
    }

    public void setEncoding(ContentEncoding encoding){
        this.encoding = encoding;
    }

    public ContentEncoding getEncoding(){
        return encoding;
    }

    @Override
    public String toString() {
        String responseBody = body == null? "" : ("\r\n\r\n" + body);

        return "HTTP/1.1 " + status + " " + message +
                "\r\nContent-Encoding: " + getEncoding().toString().toLowerCase(Locale.ROOT) +
                "\r\nContent-Type: " + getContentType() +
                "\r\nContent-Length: " + getResponseContentLength() +
                responseBody +
                "\r\n\r\n";
    }
}

package server.model;

public enum ContentType {

    TEXT_PLAIN("text/plain"),
    OCTET_STREAM("application/octet-stream"),
    NO_CONTENT_TYPE("");

    public final String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public static ContentType fromString(String contentType) {
        for (ContentType ct : ContentType.values()) {
            if (ct.contentType.equalsIgnoreCase(contentType)) {
                return ct;
            }
        }
        throw new IllegalArgumentException(contentType + " is not a valid content type");
    }
}

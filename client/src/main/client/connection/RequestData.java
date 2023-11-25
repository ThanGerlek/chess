package client.connection;

public record RequestData(String method, String path, Object request, String authTokenString) {
    public RequestData(String method, String path, Object request) {
        this(method, path, request, null);
    }

    public RequestData(String method, String path) {
        this(method, path, null, null);
    }

    public RequestData setToken(String authTokenString) {
        return new RequestData(this.method, this.path, this.request, authTokenString);
    }
}

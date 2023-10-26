package server.handlers;

import com.google.gson.Gson;
import server.http.MessageResponse;
import spark.Request;
import spark.Response;

public abstract class Handler {
    protected Gson gson = new Gson();

    public abstract Object handleRequest(Request req, Response res);

    protected String handleError(Response res, int status, String errMsg) {
        res.status(status);
        MessageResponse response = new MessageResponse(0, String.format("Error: %s", errMsg));
        return gson.toJson(response);
    }
}

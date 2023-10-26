package server.handlers;

import com.google.gson.Gson;
import server.http.MessageResponse;
import server.services.ClearApplicationService;
import spark.Request;
import spark.Response;

public class ClearApplicationHandler extends Handler {
    private static final ClearApplicationService service = new ClearApplicationService();

    @Override
    public Object handleRequest(Request req, Response res) {
        return new Gson().toJson(new MessageResponse(40, "ClearApplicationHandler"));
    }
}

/*
| **Request class**    | N/A (no request body)                                          |
| **Response class**   | MessageResponse                                                |
 */
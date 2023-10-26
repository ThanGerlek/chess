package server.handlers;

import dataAccess.DataAccessException;
import server.http.MessageResponse;
import server.services.ClearApplicationService;
import spark.Request;
import spark.Response;

public class ClearApplicationHandler extends Handler {
    private static final ClearApplicationService service = new ClearApplicationService();

    @Override
    protected Object route(Request req, Response res) throws DataAccessException {
        MessageResponse body = service.clearApplication();
        res.status(200);
        return gson.toJson(body);
    }
}

/*
| **Request class**    | N/A (no request body)                                          |
| **Response class**   | MessageResponse                                                |
 */
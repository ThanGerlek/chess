package server.handlers;

import dataAccess.DataAccessException;
import server.AuthToken;
import server.http.MessageResponse;
import server.services.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {
    private static final LogoutService service = new LogoutService();

    @Override
    public Object route(Request req, Response res) throws DataAccessException {
        AuthToken authToken = gson.fromJson(req.headers("token"), AuthToken.class);
        MessageResponse body = service.logout(authToken);
        res.status(200);
        return gson.toJson(body);
    }
    // TODO Auth
}

/*

| **Request class**    | N/A (no request body)                           |
| **Response class**   | MessageResponse                                 |
| **Headers**          | `authorization: <authToken>`                    |
 */

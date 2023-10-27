package server.handlers;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import server.AuthToken;
import server.http.MessageResponse;
import server.services.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {
    private final LogoutService service;

    public LogoutHandler(AuthDAO authDAO) {
        service = new LogoutService(authDAO);
    }

    @Override
    public Object route(Request req, Response res) throws DataAccessException {
        AuthToken authToken = gson.fromJson(req.headers("authorization"), AuthToken.class);
        MessageResponse response = service.logout(authToken);
        return parseToBody(res, response, 200);
    }
}

/*

| **Request class**    | N/A (no request body)                           |
| **Response class**   | MessageResponse                                 |
| **Headers**          | `authorization: <authToken>`                    |
 */

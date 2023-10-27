package server.handlers;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import server.http.MessageResponse;
import server.services.ClearApplicationService;
import spark.Request;
import spark.Response;

public class ClearApplicationHandler extends Handler {
    private final ClearApplicationService service;

    public ClearApplicationHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        service = new ClearApplicationService(authDAO, gameDAO, userDAO);
    }

    @Override
    protected Object route(Request req, Response res) throws DataAccessException {
        MessageResponse response = service.clearApplication();
        return parseToBody(res, response, 200);
    }
}

/*
| **Request class**    | N/A (no request body)                                          |
| **Response class**   | MessageResponse                                                |
 */
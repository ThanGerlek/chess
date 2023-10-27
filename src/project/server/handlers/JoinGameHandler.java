package server.handlers;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import server.AuthToken;
import server.http.JoinGameRequest;
import server.http.MessageResponse;
import server.services.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler {
    private final JoinGameService service;

    public JoinGameHandler(AuthDAO authDAO, GameDAO gameDAO) {
        service = new JoinGameService(authDAO, gameDAO);
    }

    @Override
    protected Object route(Request req, Response res) throws DataAccessException {
        JoinGameRequest joinGameRequest = gson.fromJson(req.body(), JoinGameRequest.class);
        AuthToken authToken = gson.fromJson(req.headers("authorization"), AuthToken.class);
        MessageResponse body = service.joinGame(joinGameRequest, authToken);
        res.status(200);
        return gson.toJson(body);
    }
}

/*

| **Request class**    | JoinGameRequest
| **Response class**   | MessageResponse
| **Headers**          | `authorization: <authToken>`
| **Body**             | `{ "playerColor":"WHITE/BLACK", "gameID": 1234 }`
 */
package server.handlers;

import dataAccess.DataAccessException;
import server.http.JoinGameRequest;
import server.http.MessageResponse;
import server.services.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler {
    private static final JoinGameService service = new JoinGameService();

    @Override
    protected Object route(Request req, Response res) throws DataAccessException {
        JoinGameRequest joinGameRequest = gson.fromJson(req.body(), JoinGameRequest.class);
        MessageResponse body = service.joinGame(joinGameRequest, req.headers("username"));
        res.status(200);
        return gson.toJson(body);
    }
    // TODO Auth
}

/*

| **Request class**    | JoinGameRequest
| **Response class**   | MessageResponse
| **Headers**          | `authorization: <authToken>`
| **Body**             | `{ "playerColor":"WHITE/BLACK", "gameID": 1234 }`
 */
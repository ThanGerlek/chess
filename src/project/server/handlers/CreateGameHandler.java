package server.handlers;

import dataAccess.DataAccessException;
import server.http.CreateGameRequest;
import server.http.CreateGameResponse;
import server.services.CreateGameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler {
    private static final CreateGameService service = new CreateGameService();

    @Override
    protected Object route(Request req, Response res) throws DataAccessException {
        CreateGameRequest createGameRequest = gson.fromJson(req.body(), CreateGameRequest.class);
        CreateGameResponse body = service.createGame(createGameRequest);
        res.status(200);
        return gson.toJson(body);
    }
    // TODO auth check
}

/*

| **Request class**    | CreateGameRequest                            |
| **Response class**   | CreateGameResponse                           |
| **Description**      | Creates a new game.                          |
| **Headers**          | `authorization: <authToken>`                 |
| **Body**             | `{ "gameName":"" }`                          |
 */

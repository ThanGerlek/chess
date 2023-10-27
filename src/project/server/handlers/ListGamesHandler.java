package server.handlers;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import server.http.ListGamesResponse;
import server.services.ListGamesService;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends Handler {
    private final ListGamesService service;

    public ListGamesHandler(GameDAO gameDAO) {
        service = new ListGamesService(gameDAO);
    }

    @Override
    public Object route(Request req, Response res) throws DataAccessException {
        ListGamesResponse body = service.listGames();
        res.status(200);
        return gson.toJson(body);
    }
    // TODO Auth
}

/*
Note that `whiteUsername` and `blackUsername` may be `null`.

| **Request class**    | N/A (no request body)
| **Response class**   | ListGamesResponse
| **Headers**          | `authorization: <authToken>`
 */

package server.handlers;

import com.google.gson.Gson;
import server.http.MessageResponse;
import server.services.ListGamesService;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends Handler {
    private static final ListGamesService service = new ListGamesService();

    @Override
    public Object handleRequest(Request req, Response res) {
        return new Gson().toJson(new MessageResponse(40, "ListGamesHandler"));
    }
}

/*
Note that `whiteUsername` and `blackUsername` may be `null`.

| **Request class**    | N/A (no request body)
| **Response class**   | ListGamesResponse
| **Headers**          | `authorization: <authToken>`
 */

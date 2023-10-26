package server.handlers;

import com.google.gson.Gson;
import server.http.MessageResponse;
import server.services.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler {
    private static final JoinGameService service = new JoinGameService();

    @Override
    public Object handleRequest(Request req, Response res) {
        return new Gson().toJson(new MessageResponse(40, "JoinGameHandler"));
    }
}

/*

| **Request class**    | JoinGameRequest
| **Response class**   | MessageResponse
| **Headers**          | `authorization: <authToken>`
| **Body**             | `{ "playerColor":"WHITE/BLACK", "gameID": 1234 }`
 */
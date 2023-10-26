package server.handlers;

import com.google.gson.Gson;
import server.http.MessageResponse;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler {
    @Override
    public Object handleRequest(Request req, Response res) {
        return new Gson().toJson(new MessageResponse(40, "CreateGameHandler"));
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

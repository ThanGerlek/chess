package server.handlers;

import com.google.gson.Gson;
import server.http.MessageResponse;
import server.services.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {
    private static final RegisterService service = new RegisterService();

    @Override
    public Object handleRequest(Request req, Response res) {
        return new Gson().toJson(new MessageResponse(40, "RegisterHandler"));
    }
}

/*
| **Request class**    | RegisterRequest                                |
| **Response class**   | AuthResponse                                   |
| **Body**             | `{ "username":"", "password":"", "email":"" }` |
*/

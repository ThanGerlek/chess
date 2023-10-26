package server.handlers;

import com.google.gson.Gson;
import server.http.MessageResponse;
import server.services.LoginService;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler {
    private static final LoginService service = new LoginService();

    @Override
    public Object handleRequest(Request req, Response res) {
        return new Gson().toJson(new MessageResponse(40, "LoginHandler"));
    }
}

/*

| **Request class**    | LoginRequest                                        |
| **Response class**   | AuthResponse                                        |
| **Body**             | `{ "username":"", "password":"" }`                  |
 */

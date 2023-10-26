package server.handlers;

import dataAccess.DataAccessException;
import server.http.AuthResponse;
import server.http.LoginRequest;
import server.services.LoginService;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler {
    private static final LoginService service = new LoginService();

    @Override
    public Object route(Request req, Response res) throws DataAccessException {
        LoginRequest loginRequest = gson.fromJson(req.body(), LoginRequest.class);
        AuthResponse body = service.login(loginRequest);
        res.status(200);
        return gson.toJson(body);
    }
}

/*

| **Request class**    | LoginRequest                                        |
| **Response class**   | AuthResponse                                        |
| **Body**             | `{ "username":"", "password":"" }`                  |
 */

package server.handlers;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import server.http.AuthResponse;
import server.http.RegisterRequest;
import server.services.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {
    private final RegisterService service;

    public RegisterHandler(AuthDAO authDAO, UserDAO userDAO) {
        service = new RegisterService(authDAO, userDAO);
    }

    @Override
    public Object route(Request req, Response res) throws DataAccessException {
        RegisterRequest registerRequest = gson.fromJson(req.body(), RegisterRequest.class);
        AuthResponse body = service.register(registerRequest);
        res.status(200);
        return gson.toJson(body);
    }
}

/*
| **Request class**    | RegisterRequest                                |
| **Response class**   | AuthResponse                                   |
| **Body**             | `{ "username":"", "password":"", "email":"" }` |
*/

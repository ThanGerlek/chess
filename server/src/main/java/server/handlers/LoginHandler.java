package server.handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import http.LoginRequest;
import server.services.LoginService;

public class LoginHandler extends HttpHandler<LoginService> {
    public LoginHandler(LoginService service) {
        super(service);
    }

    @Override
    protected Object getResponse(String body, String authToken) throws DataAccessException {
        LoginRequest request = new Gson().fromJson(body, LoginRequest.class);
        return getService().login(request);
    }
}

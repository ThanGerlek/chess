package server.handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import http.RegisterRequest;
import server.services.RegisterService;


public class RegisterHandler extends HttpHandler<RegisterService> {
    public RegisterHandler(RegisterService service) {
        super(service);
    }

    @Override
    protected Object getResponse(String body, String authToken) throws DataAccessException {
        RegisterRequest request = new Gson().fromJson(body, RegisterRequest.class);
        return getService().register(request);
    }
}

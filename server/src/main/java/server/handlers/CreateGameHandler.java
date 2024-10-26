package server.handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import http.CreateGameRequest;
import server.services.CreateGameService;

public class CreateGameHandler extends HttpHandler<CreateGameService> {
    public CreateGameHandler(CreateGameService service) {
        super(service);
    }

    @Override
    protected Object getResponse(String body, String authToken) throws DataAccessException {
        CreateGameRequest request = new Gson().fromJson(body, CreateGameRequest.class);
        return getService().createGame(request, authToken);
    }
}

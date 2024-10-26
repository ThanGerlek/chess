package server.handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import http.JoinGameRequest;
import server.services.JoinGameService;

public class JoinGameHandler extends HttpHandler<JoinGameService> {
    public JoinGameHandler(JoinGameService service) {
        super(service);
    }

    @Override
    protected Object getResponse(String body, String authToken) throws DataAccessException {
        JoinGameRequest request = new Gson().fromJson(body, JoinGameRequest.class);
        return getService().joinGame(request, authToken);
    }
}

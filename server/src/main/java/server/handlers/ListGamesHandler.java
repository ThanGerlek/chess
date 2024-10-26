package server.handlers;

import dataaccess.DataAccessException;
import server.services.ListGamesService;

public class ListGamesHandler extends HttpHandler<ListGamesService> {
    public ListGamesHandler(ListGamesService service) {
        super(service);
    }

    @Override
    protected Object getResponse(String body, String authToken) throws DataAccessException {
        return getService().listGames(authToken);
    }
}

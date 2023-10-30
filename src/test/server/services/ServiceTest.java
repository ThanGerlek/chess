package server.services;

import dataAccess.*;
import server.AuthToken;
import server.Game;
import server.User;

abstract class ServiceTest {
    protected AuthDAO authDAO;
    protected GameDAO gameDAO;
    protected UserDAO userDAO;

    void initDAOs() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO(userDAO);
        gameDAO = new MemoryGameDAO(userDAO);
    }

    void fillDatabases() throws DataAccessException {
        User user1 = new User("user1", "pass1", "mail1");
        User user2 = new User("user2", "pass2", "mail2");
        Game game1 = new Game(1, "game1");
        Game game2 = new Game(2, "game2");
        AuthToken token1 = new AuthToken("1234", "user1");
        AuthToken token2 = new AuthToken("2468", "user2");

        userDAO.insertNewUser(user1);
        userDAO.insertNewUser(user2);
        gameDAO.insertNewGame(game1);
        gameDAO.insertNewGame(game2);
        authDAO.addAuthToken(token1);
        authDAO.addAuthToken(token2);
    }
}
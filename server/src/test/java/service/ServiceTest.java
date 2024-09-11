package service;

import dataaccess.*;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

abstract class ServiceTest {
    protected AuthDAO authDAO;
    protected GameDAO gameDAO;
    protected UserDAO userDAO;

    void initDAOs() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO(userDAO);
        gameDAO = new MemoryGameDAO(userDAO);
    }

    User createTestUser(String username, String password, String email) {
        String pwHash = BCrypt.hashpw(password, BCrypt.gensalt());
        return new User(username, pwHash, email);
    }

}
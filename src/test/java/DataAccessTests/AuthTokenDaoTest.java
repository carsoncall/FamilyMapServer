package DataAccessTests;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import Model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;

public class AuthTokenDaoTest {
    private Database db;
    private AuthToken bestAuthToken;
    private AuthToken secondAuthToken;
    private AuthTokenDao aDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        bestAuthToken = new AuthToken("blabla123","nonsense");
        secondAuthToken = new AuthToken("helloworld","lol");
        Connection conn = db.getConnection();
        aDao = new AuthTokenDao(conn);
        aDao.clear();
    }

    @AfterEach
    public void tearDown() {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        aDao.insert(bestAuthToken);
        String compareTest = aDao.find(bestAuthToken.getAuthToken());
        assertNotNull(compareTest);
        assertEquals(bestAuthToken.getUsername(),compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //tests that you cannot insert two AuthToken objects with the same AuthToken field.
        aDao.insert(bestAuthToken);
        assertThrows(DataAccessException.class, ()-> aDao.insert(bestAuthToken));
    }

    @Test
    public void findPass() throws DataAccessException {
        aDao.insert(bestAuthToken);
        String username = aDao.find(bestAuthToken.getAuthToken());
        assertNotNull(username);
        assertEquals(bestAuthToken.getUsername(),username);
    }

    @Test
    public void findFail() throws DataAccessException {
        aDao.insert(bestAuthToken);
        String username = aDao.find(secondAuthToken.getAuthToken());
        assertNull(username);
    }

    @Test
    public void clearPass() throws DataAccessException {
        //tests whether the clear function removes all AuthToken objects from the DB
        aDao.insert(bestAuthToken);
        aDao.insert(secondAuthToken);
        aDao.clear();
        assertNull(aDao.find(bestAuthToken.getAuthToken()));
        assertNull(aDao.find(secondAuthToken.getAuthToken()));
    }
}

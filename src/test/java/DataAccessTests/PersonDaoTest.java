package DataAccessTests;


import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDao;
import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class PersonDaoTest {
    private Database db;
    private Person bestPerson;
    private Person secondBestPerson;
    private PersonDao pDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        // Here we can set up any classes or variables we will need for each test
        // lets create a new instance of the Database class
        db = new Database();
        // and a new event with random data
        bestPerson = new Person("bigdaddy123", "bigson 456", "Giga","Chad",
                "m", "biggrandpa123","biggrandma123","bigmama123");
        secondBestPerson = new Person("SnoopDawg","snoopdawg","weed","smoker",
                "m","whoknows","whoknowsagain","themoney");

        // Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO, so it can access the database.
        pDao = new PersonDao(conn);
        //Let's clear the database as well so any lingering data doesn't affect our tests
        pDao.clear();
    }

    @AfterEach
    public void tearDown() {
        // Here we close the connection to the database file, so it can be opened again later.
        // We will set commit to false because we do not want to save the changes to the database
        // between test cases.
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        // Start by inserting an event into the database.
        pDao.insert(bestPerson);
        // Let's use a find method to get the event that we just put in back out.
        Person compareTest = pDao.find(bestPerson.getPersonID(),bestPerson.getAssociatedUsername());
        // First lets see if our find method found anything at all. If it did then we know that we got
        // something back from our database.
        assertNotNull(compareTest);
        // Now lets make sure that what we put in is the same as what we got out. If this
        // passes then we know that our insert did put something in, and that it didn't change the
        // data in any way.
        // This assertion works by calling the equals method in the Event class.
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        // Let's do this test again, but this time lets try to make it fail.
        // If we call the method the first time the event will be inserted successfully.
        pDao.insert(bestPerson);

        // However, our sql table is set up so that the column "eventID" must be unique, so trying to insert
        // the same event again will cause the insert method to throw an exception, and we can verify this
        // behavior by using the assertThrows assertion as shown below.

        // Note: This call uses a lambda function. A lambda function runs the code that comes after
        // the "()->", and the assertThrows assertion expects the code that ran to throw an
        // instance of the class in the first parameter, which in this case is a DataAccessException.
        assertThrows(DataAccessException.class, () -> pDao.insert(bestPerson));
    }

    @Test
    public void retrievePass() throws DataAccessException {
        //Tests if the retrieval is not simply finding the first person in the DB
        pDao.insert(bestPerson);
        pDao.insert(secondBestPerson);
        Person theGuy = pDao.find(bestPerson.getPersonID(),bestPerson.getAssociatedUsername());
        assertNotEquals(secondBestPerson,theGuy);
        assertEquals(bestPerson,theGuy);
    }

    @Test
    public void retrieveFail() throws DataAccessException {
        //Tests if the retrieval returns null if the user has not been inserted
        pDao.insert(bestPerson);
        Person theGuy = pDao.find(secondBestPerson.getPersonID(),secondBestPerson.getAssociatedUsername());
        assertNull(theGuy);
    }

    @Test
    public void clearPass() throws DataAccessException {
        // Tests that the database is clear by adding two users and then checking that both are null after .clear()
        pDao.insert(secondBestPerson);
        pDao.insert(bestPerson);

        pDao.clear();

        Person theGuy = pDao.find(bestPerson.getPersonID(),bestPerson.getAssociatedUsername());
        Person theOtherGuy = pDao.find(secondBestPerson.getPersonID(),secondBestPerson.getAssociatedUsername());

        assertNull(theGuy);
        assertNull(theOtherGuy);
    }

    @Test
    public void allFamilyPass() throws DataAccessException {
        //tests whether the database will correctly find all people with an associated username
        List<Person> personList = new ArrayList<>();
        List<Person> testList;
        personList.add(bestPerson);
        secondBestPerson.setAssociatedUsername(bestPerson.getAssociatedUsername());
        personList.add(secondBestPerson);
        assertEquals(bestPerson.getAssociatedUsername(),secondBestPerson.getAssociatedUsername());

        pDao.insert(bestPerson);
        pDao.insert(secondBestPerson);

        testList = pDao.allFamily(bestPerson.getAssociatedUsername());
        assertNotNull(testList);
        assertEquals(personList,testList);
    }
}

package Service;

import DataAccess.*;
import Model.Person;
import Request.PersonRequest;
import Result.PersonResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Performs the Person (/person/[personID]) function.
 */
public class PersonService {

    /**
     * Returns the single Person object with the specified ID (if the person is associated with the current user).
     * The current user is determined by the provided authtoken.
     * @param personRequest the container with the necessary data
     * @return the container with the result data.
     */
    public PersonResult retrieve(PersonRequest personRequest) {
        Database db = new Database();

        try (Connection conn = db.getConnection()) {
            //First we check if the provided AuthToken is valid
            AuthTokenDao authTokenDao;
            authTokenDao = new AuthTokenDao(conn);
            String username = authTokenDao.find(personRequest.getAuth());

            //If the .find function returned a username, that means the authtoken exists
            if (username != null) {
                PersonDao personDao;
                personDao = new PersonDao(conn);

                //If .getPersonID() is null, that means the user did not provide a PersonID
                if (personRequest.getPersonID() == null) {
                    PersonResult personResult = new PersonResult();
                    List<Person> result = personDao.allFamily(username);
                    Person[] resultArray = result.toArray(new Person[0]);
                    personResult.setData(resultArray);
                    return personResult;
                } else {
                    //If they did provide one, then we need a specific person, not a list of people.
                    Person person = personDao.find(personRequest.getPersonID(),username);
                    if (person != null) {
                        return new PersonResult(person.getAssociatedUsername(), person.getPersonID()
                                , person.getFirstName(), person.getLastName(), person.getGender(), person.getFatherID()
                                , person.getMotherID(), person.getSpouseID());
                    } else {
                        //Error if the personID does not exist (including if it exists, but not with the User's username)
                        PersonResult personResult = new PersonResult();
                        personResult.setMessage("Error: The provided Person ID does not exist");
                        personResult.setSuccess(false);
                        return personResult;
                    }
                }
            } else {
                //Error if the authentication token does not exist
                PersonResult personResult = new PersonResult();
                personResult.setMessage("Error: The provided authentication is invalid");
                personResult.setSuccess(false);
                return personResult;
            }

        } catch (DataAccessException | SQLException d) {
            System.out.print("Error: PersonService failed to access the database");
            d.printStackTrace();
        }
        //This is the error that shows when something failed in the try block
        PersonResult personResult = new PersonResult();
        personResult.setMessage("Error: unable to retrieve person");
        personResult.setSuccess(false);
        return personResult;
    }
}

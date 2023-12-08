package Service;

import DataAccess.*;
import Model.Event;
import Model.Person;
import Model.User;
import Request.FillRequest;
import Result.FillResult;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

/**
 * Contains the Fill (/fill/[username]/[generations]) function.
 */
public class FillService {

    //These are data that are used in multiple functions
    Names femaleNamesArray = null;
    Names maleNamesArray = null;
    Names lastNamesArray = null;
    Locations locationsArray = null;
    User user = null;
    Database db = new Database();
    int numPeopleAdded = 0;
    int numEventsAdded = 0;

    /**
     * Populates the server's database with generated data for the specified username. The required "username" parameter
     * must be a user already registered with the server. If there is any data in the database already associated with
     * the given username, it is deleted.
     * The optional "generations" parameter lets the caller specify the number of generations of ancestors to be
     * generated, and must be a non-negative integer (the default is 4, which results in 31 new persons each with
     * associated events).
     * @param fillRequest a container with the data from the user's request.
     * @return A container with the resulting data indicating success or failure.
     */
    public FillResult fill(FillRequest fillRequest) {

        //first, we retrieve the User object for later use (also to check that it exists)
        try (Connection conn = db.getConnection()){
            UserDao userDao = new UserDao(conn);
            user = userDao.find(fillRequest.getUsername());
            if (user != null) {

                try {
                    //This is where we clear all of the Persons and Events associated with the username
                    PersonDao personDao = new PersonDao(conn);
                    personDao.clear(user.getUsername());

                    EventDao eventDao = new EventDao(conn);
                    eventDao.clear(user.getUsername());

                    db.closeConnection(true);

                } catch (DataAccessException d) {
                    System.out.print("Failed to clear user's data in FillService");
                    d.printStackTrace();
                }

                //These are the files that were provided for making random information
                File femaleNames = new File("json/fnames.json");
                File maleNames = new File("json/mnames.json");
                File lastNames = new File("json/snames.json");
                File locations = new File("json/locations.json");


                // first we will try to load the information from the given JSONs into objects that we can access
                if (femaleNames.exists() && maleNames.exists() && lastNames.exists() && locations.exists()) {
                    try {
                        femaleNamesArray = new Gson().fromJson(Files.readString(femaleNames.toPath()), Names.class);
                        maleNamesArray = new Gson().fromJson(Files.readString(maleNames.toPath()), Names.class);
                        lastNamesArray = new Gson().fromJson(Files.readString(lastNames.toPath()), Names.class);
                        locationsArray = new Gson().fromJson(Files.readString(locations.toPath()), Locations.class);
                    } catch (IOException e) {
                        System.out.print("The random information files could not be read\n");
                        e.printStackTrace();
                    }

                    //each person has 3 events: birth, marriage, and death
                    //user also has a birth event
                    //0 = just user's event
                    //1 = 1 generation,
                    // ...continuing for up to 5 generations

                    //This function creates all people and the associated events
                    generatePerson(user.getGender(), fillRequest.getGenerations(),0);

                } else {
                    System.out.print("JSON's containing the random data pools were not found");
                }

            } else {
                //username does not exist
                FillResult result = new FillResult();
                result.setMessage("Error: The username provided does not exist in the database");
                result.setSuccess(false);
                return result;
            }
        } catch (DataAccessException | SQLException d ) {
            System.out.print("There was an error when the FillHandler tried to find the username in the" +
                    "database");
            d.printStackTrace();
        }
        //successful fill result
        FillResult result = new FillResult();
        result.setMessage("Successfully added " + numPeopleAdded + " persons and " +
                                numEventsAdded + " events to the database.");
        result.setSuccess(true);
        return result;
    }

    /**
     * This function recursively creates people, with 3 events each (birth, marriage, and death). It only creates a
     * birth and *death* event for the current user (bwahahaha)
     * @param gender the gender of the person being created
     * @param generations the number of generations to create. Note-- zero generations AND zero counter is a special
     *                    case for the User's person-- we do not make them a marriage event
     * @param counter the level of recursion. Used in calculating the year of the events. All users are given events that
     *                center around a birthdate of 1982 - counter * 20; marriage is 20 years after and death is 70
     *                years after.
     * @return A newly created Person object (for recursion purposes, the caller function does not use the Person)
     */

    private Person generatePerson(String gender, int generations, int counter) {

        Person mother = null;
        Person father = null;

        int year = 1982 - counter*20;

        //If generations >0, that means we are making more than just the User's person
        if (generations > 0) {
            mother = generatePerson("f", generations - 1, counter + 1);
            father = generatePerson("m", generations - 1, counter + 1);

            father.setSpouseID(mother.getPersonID());
            mother.setSpouseID(father.getPersonID());

            //This is where we take the newly created mother and father and use their information to make marriage
            //events that coorespond in location and year but are separate
            Location randomLocation = locationsArray.data[new Random().nextInt(locationsArray.data.length)];

            Event motherMarriage = new Event(UUID.randomUUID().toString(), user.getUsername(), mother.getPersonID()
                    ,randomLocation.latitude, randomLocation.longitude, randomLocation.country, randomLocation.city
                    ,"marriage", year+20);
            Event fatherMarriage = new Event(UUID.randomUUID().toString(),user.getUsername(),father.getPersonID()
                    ,randomLocation.latitude, randomLocation.longitude, randomLocation.country, randomLocation.city
                    ,"marriage", year+20);

            //We write the new people and the event to the database.
            try (Connection conn = db.getConnection()){
                EventDao eventDao = new EventDao(conn);
                eventDao.insert(motherMarriage);
                eventDao.insert(fatherMarriage);

                PersonDao personDao = new PersonDao(conn);
                personDao.insert(mother);
                personDao.insert(father);

                db.closeConnection(true);
                numEventsAdded += 2;
                numPeopleAdded += 2;
            } catch (DataAccessException | SQLException d) {
                System.out.print("There was an error writing random (marriage) information to the database.");
                d.printStackTrace();
            }
        }
        Person person;
        //this is the special case where generations == 0 and counter == 0 -- we make the User person
        if (counter == 0) {
            person = new Person(user.getPersonID(), user.getUsername(), user.getFirstName()
                    , user.getLastName(), gender, null, null, null);

            //If we created a mother and father (recursively) then we add their ID's here
            if (father != null) {
                person.setFatherID(father.getPersonID());
                person.setMotherID(mother.getPersonID());
            }

            //writing this person to the database
            try (Connection conn = db.getConnection()){
                PersonDao personDao = new PersonDao(conn);
                personDao.insert(person);

                db.closeConnection(true);
                numPeopleAdded += 1;
            } catch (DataAccessException | SQLException d) {
                System.out.print("There was an error writing random (person) information to the database.");
                d.printStackTrace();
            }

        } else {
            //Generating a single person who is random
            String firstName;
            String lastName;
            if (gender.equals("m")) {
                firstName = maleNamesArray.data[new Random().nextInt(maleNamesArray.data.length)];
            } else {
                firstName = femaleNamesArray.data[new Random().nextInt(femaleNamesArray.data.length)];
            }
            lastName = lastNamesArray.data[new Random().nextInt(lastNamesArray.data.length)];
            person = new Person(UUID.randomUUID().toString(), user.getUsername(), firstName
                    , lastName, gender, null, null, null);
            //if generations = 0, parents might be null
            if (father != null) {
                person.setFatherID(father.getPersonID());
                person.setMotherID(mother.getPersonID());
            }
        }

        //This is where we create random birth and death events for every person who is created, regardless of whether
        //they are User persons or random persons.
        Location randomBirthLocation = locationsArray.data[new Random().nextInt(locationsArray.data.length)];

        Event personBirth = new Event(UUID.randomUUID().toString(), user.getUsername(), person.getPersonID()
                , randomBirthLocation.latitude, randomBirthLocation.longitude, randomBirthLocation.country
                , randomBirthLocation.city, "birth", year);

        Event personDeath = new Event(UUID.randomUUID().toString(), user.getUsername(), person.getPersonID()
                , randomBirthLocation.latitude, randomBirthLocation.longitude, randomBirthLocation.country
                , randomBirthLocation.city, "death", year + 70);

        //This is where we write those events to the database
        try (Connection conn = db.getConnection()) {
            EventDao eventDao = new EventDao(conn);
            eventDao.insert(personBirth);
            eventDao.insert(personDeath);
            db.closeConnection(true);
            numEventsAdded += 2;
        } catch (DataAccessException | SQLException d) {
            System.out.print("Could not write birth or death events to database from FillService");
            d.printStackTrace();
        }
        return person;
    }

    //These are small "data bags" for holding the data from the JSONs.
    private class Names {
        String[] data;
    }

    private class Locations {
        Location[] data;
    }

    private class Location {
        String country;
        String city;
        float latitude;
        float longitude;
    }

}

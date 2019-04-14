import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

    /*@Before
    public void setup() {
        Fixtures.deleteDatabase();
    }*/

    @Test
    public void createAndRetrieveClient() {
        Client ivan = new Client("Ivan", "Ivanov", "89003431234", "vano@google.com", false).save();
        new Client("FSfds", "fdsfds", "89003431234", "vdsgle.com", false).save();

        Employee manager = new Employee("Petr", "Petrov", "manager", "89003431234", "vano@google.com", 40000, 0.05).save();
        Employee engineer = new Employee("Tttt", "Cccc", "engineer", "89003431234", "vano@google.com", 40000, 0.05).save();
        new Employee("Zzzz", "Ssss", "engineer", "89003431234", "vano@google.com", 40000, 0.05).save();

        Trouble trouble = new Trouble("Some trouble").save();
        Trouble trouble2 = new Trouble("Another trouble").save();


        new Project(ivan, engineer, manager, 10, 30, new Date(2019, 4, 14), new Date(2019, 4, 15), false, trouble,true).save();
        new Project(ivan, engineer, manager, 20, 60, new Date(2019, 4, 14), new Date(2019, 4, 15), false, trouble,true).save();
        new Project(ivan, engineer, manager, 345, 435, new Date(2019, 4, 12), new Date(2019, 4, 13), false, trouble2,true).save();


        assertNotNull(ivan);
        assertEquals("Ivan", ivan.firstName);
    }

}

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
        new Client("Ivan", "Ivanov", "89003431234", "vano@google.com", false).save();
        new Client("FSfds", "fdsfds", "89003431234", "vdsgle.com", false).save();

        new Employee("Ivan", "Ivanov", "manager", "89003431234", "vano@google.com", 40000, 0.05).save();
        new Employee("Tttt", "Cccc", "engineer", "89003431234", "vano@google.com", 40000, 0.05).save();
        new Employee("Zzzz", "Ssss", "engineer", "89003431234", "vano@google.com", 40000, 0.05).save();


        Client ivan = Client.find("byEmail", "vano@google.com").first();
        assertNotNull(ivan);
        assertEquals("Ivan", ivan.firstName);
    }

}

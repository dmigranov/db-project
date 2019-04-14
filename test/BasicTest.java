import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {


    @Test
    public void createAndRetrieveClient() {
        new Client("Ivan", "Ivanov", "89003431234", "vano@google.com", false).save();

        Client ivan = Client.find("byEmail", "vano@google.com").first();
        assertNotNull(ivan);
        assertEquals("Ivan", ivan.firstName);
    }

}
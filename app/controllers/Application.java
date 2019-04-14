package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        Client client = Client.find("order by firstName desc").first();

        render(client);

    }

    public static void salary() {
        List<Employee> employees = Employee.find("order by salary desc").first();

        render(employees);

    }

}
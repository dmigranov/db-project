package controllers;

import oracle.ons.Cli;
import org.hibernate.exception.SQLGrammarException;
import play.*;
import play.data.validation.Valid;
import play.db.DB;
import play.mvc.*;

import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.List;

import models.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

public class Application extends Controller {

    public static void index() {
        Client client = Client.find("order by firstName desc").first();

        render(client);
    }

    public static void salary() {
        List<Employee> employees = Employee.find("order by salary desc").fetch();

        render(employees);
    }

    public static void troubles() throws SQLException {
        /*List troubles = Project.find(
                "select t.name, count(*) as trouble_count FROM Project p JOIN Trouble t ON p.trouble = t GROUP BY t.id, t.name"
        ).fetch();*/

        Connection conn = DB.getConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet  = statement.executeQuery("select t.name as t_name, trouble_count FROM Trouble t JOIN (select t.id as id, count(*) as trouble_count FROM Project p JOIN Trouble t ON p.trouble_id = t.id GROUP BY t.id) tc ON t.id = tc.id ORDER BY trouble_count DESC");
        //= null;
        List<Object[]> resultList = new ArrayList<>();
            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                resultList.add(new Object[]{resultSet.getString("t_name"), resultSet.getInt("trouble_count")});
            }

        render(resultList);
    }

    public void fillDB() {
        Client ivan = new Client("Ivan", "Ivanov", "89003431234", "vano@google.com", false).save();
        new Client("FSfds", "fdsfds", "89003431234", "vdsgle.com", false).save();

        Employee manager = new Employee("Petr", "Petrov", "manager", "89003431234", "vano@google.com", 40000, 0.05).save();
        Employee engineer = new Employee("Tttt", "Cccc", "engineer", "89003431234", "vano@google.com", 40000, 0.05).save();
        new Employee("Zzzz", "Ssss", "engineer", "89003431234", "vano@google.com", 40000, 0.05).save();

        Trouble trouble = new Trouble("Some trouble").save();
        Trouble trouble2 = new Trouble("Another trouble").save();

        new Project(ivan, engineer, manager, 10, 30, new Date(2019, 4, 14), new Date(2019, 4, 15), false, trouble,null,true).save();
        new Project(ivan, engineer, manager, 20, 60, new Date(2019, 4, 14), new Date(2019, 4, 15), false, trouble,null,true).save();
        new Project(ivan, engineer, manager, 345, 435, new Date(2019, 4, 12), new Date(2019, 4, 13), false, trouble2,null, true).save();

        /*Client c = new Client("FSsf", "fdsfsdf", "79003431234", "vano@nsi.com", false);
        if(!validation.valid(c).ok)
        {
            System.out.println("hellloooo");
        }
        else
        {
            c.save();
        }*/

        /*Employee manager = new Employee("Petr", "Petrov", "manager", "89003431234", "vano@google.com", 40000, 0.05).save();
        System.out.println(validation.valid(manager).ok);
        Employee engineer = new Employee("Tttt", "Cccc", "fdsfsdf", "89003431234", "vano@google.com", 40000, 0.05).save();
        System.out.println(validation.valid(engineer).ok);
        */

        /*DetailType processor = new DetailType("processor");
        DetailType motherboard = new DetailType("motherboard");
        DetailType ram = new DetailType("ram");
        DetailType videocard = new DetailType("video card");
        DetailType storage = new DetailType("storage");
        DetailType powersupply = new DetailType("power supply");
        DetailType networkcard = new DetailType("network card");
        DetailType diskstorage = new DetailType("disk storage");

//        DetailType type, int cost, String name, String description, int count
        Detail detail1 = new Detail(processor, 10000, "Intel Core i7", "very good processor", 3);
        Detail detail2 = new Detail(processor, 11000, "AMD Ryzen 7", "not very good processor", 2);
        Detail detail3 = new Detail(videocard, 20000, "Nvidia GeForce 1660", "top videocard", 10);
        Detail detail4 = new Detail(videocard, 15000, "Nvidia GeForce 1080", "old videocard", 5);

        processor.save();
        motherboard.save();
        ram.save();
        videocard.save();
        storage.save();
        powersupply.save();
        networkcard.save();
        diskstorage.save();


        detail1.save();
        detail2.save();
        detail3.save();
        detail4.save();*/

        //new DetailOrder((Project)Project.findAll().get(0), (Detail)Detail.findAll().get(5), 2).save();

    }

    ///выводит список всех проектов, на которых задействован работник
    public static void projectsOfEmployee(long id, Date startDate, Date endDate) {

        List projects = null;
        String addition = "";


        if(startDate == null && endDate == null) {
            projects = Project.find(
                    "Select p, c.firstName, c.lastName FROM Project p JOIN Client c ON p.client = c  where p.engineer.id = ?1 or p.manager.id = ?1", id
            ).fetch();
        }
        else
        {
            if(startDate == null) {
                projects = Project.find(
                        "Select p, c.firstName, c.lastName FROM Project p JOIN Client c ON p.client = c  where p.engineer.id = ?1 or p.manager.id = ?1 and p.workBegin < ?2", id, endDate
                ).fetch();
            }
            else if(endDate == null) {
                projects = Project.find(
                        "Select p, c.firstName, c.lastName FROM Project p JOIN Client c ON p.client = c  where p.engineer.id = ?1 or p.manager.id = ?1 and p.workBegin > ?2", id, startDate
                ).fetch();
            }
            else
                projects = Project.find(
                        "Select p, c.firstName, c.lastName FROM Project p JOIN Client c ON p.client = c  where p.engineer.id = ?1 or p.manager.id = ?1 and p.workBegin > ?2 and p.workBegin < ?3", id, startDate, endDate
                ).fetch();

        }

        Employee employee = Employee.findById(id);
        List employees = Employee.findAll();

        render(projects, employee, employees);
    }



    ///выводит список всех проектов и сортирует их
    public static void projects(int sortType, int desc) {   //sortType = 0 - по сумме стимостей, 2 - по типу; desc = 0 - asc, desc = 1 = desc
        List projects = null;
        String order = (desc == 1 ? " desc" : " asc");
        if(sortType == 0) {     //по стоимости
            projects = Project.find(
                    "Select p, c.firstName, c.lastName, (detailCost + workCost) as cost FROM Project p LEFT JOIN Client c ON p.client = c ORDER BY cost" + order
            ).fetch();
        }
        else
        {
            projects = Project.find(
                    "Select p, c.firstName, c.lastName, (detailCost + workCost) as cost FROM Project p LEFT JOIN Client c ON p.client = c ORDER BY type" + order
            ).fetch();
        }

        List<Client> clients = Client.findAll();
        List<Employee> managers = Employee.find("select e from Employee e where e.position = 'manager'").fetch();
        List<Employee> engineers = Employee.find("select e from Employee e where e.position = 'engineer'").fetch();
        List<Trouble> troubles = Trouble.findAll();

        String error = projectsError;
        projectsError = null;

        render(projects, clients, engineers, managers, troubles, error);
    }

    private static String projectsError = null;
    public static void addProject(int workCost, Date workBegin, Date workEnd, boolean isGuaranteed, long client_id, long manager_id, long engineer_id, long trouble_id, String description, boolean type)
    {
        Employee manager = Employee.findById(manager_id);
        Employee engineer = Employee.findById(engineer_id);
        Client client = Client.findById(client_id);
        Trouble trouble = Trouble.findById(trouble_id);

        boolean success = false;
        Project p = null;
        if(manager == null || engineer == null || client == null || trouble == null)
            projectsError = "Oops...";
        else {
            if (workBegin != null)
            {
                if(workEnd == null || (workEnd != null && workBegin.before(workEnd))) {
                    p = new Project(client, engineer, manager, 0, workCost, workBegin, workEnd, isGuaranteed, trouble, description, type);
                    if (!validation.valid(p).ok) {
                        projectsError = "Impossible to add such a project!";
                    } else {
                        p.save();
                        success = true;
                    }
                }
                else
                    projectsError = "Wrong end date!";
            }
            else
            {
                projectsError = "Please fill in start date...";
            }
        }
        if(success)
            Application.addProjectPage(p.id);
        else
            Application.projects(0, 0);
    }

    static String orderError = null;
    public static void addOrder(long id, long detail_id, int count) throws SQLException
    {
        Project project = Project.findById(id);
        Detail detail = Detail.findById(detail_id);
        if(project != null && detail != null) {
            DetailOrder order = new DetailOrder(project, detail, count);
            if (!validation.valid(order).ok) {
                orderError = "Impossible to add such an order!";
            } else {
                try {
                    order.save();
                    //project.setDetailCost(project.getDetailCost() + count * detail.cost);
                    /*long detailAdd = count * detail.cost;
                    Connection conn = DB.getConnection();
                    Statement statement = conn.createStatement();
                    int c = statement.executeUpdate("UPDATE Project SET detailCost = detailCost + " + detailAdd + " WHERE id = " + id);
                    conn.commit();*/
                    //сделал через Formula!
                }
                catch(PersistenceException e)        //триггер выкинул исключение!
                {
                    orderError = "Impossible to add such an order! Not enough details!";
                }
            }
        }
        else
            orderError = "Wrong detail!";


        addProjectPage(id);
    }

    public static void addProjectPage(long id)
    {
        Project project = Project.findById(id);
        List details = Detail.findAll();
        List orders = DetailOrder.find("SELECT o from DetailOrder o where project_id = ?1", id).fetch();
        if(project == null)
            projects(0, 0);
        else {
            String error = orderError;
            orderError = null;
            render(project, details, error, orders);
        }
    }

    public static void deleteProject(long id)
    {
        try{
            Project.delete("delete from Project where id = ?1", id);
        }
        catch(PersistenceException e)
        {
            //todo
        }

        projects(0, 0);
    }

    public static void popularDetails(long type) throws SQLException {
        //Statement statement = conn.createStatement();
        //String query = "SELECT * FROM Detail d JOIN (Select d.id as d_id, sum(coalesce(o.count, 0)) as buyCount FROM Detail d /*LEFT*/ JOIN DetailOrder o ON o.detail_id = d.id WHERE d.type_id = " + type + " GROUP BY d.id) counts ON d.id = counts.d_id ORDER BY buyCount DESC";
        Connection conn = DB.getConnection();
        String query = "SELECT * FROM Detail d JOIN (Select d.id as d_id, sum(coalesce(o.count, 0)) as buyCount FROM Detail d /*LEFT*/ JOIN DetailOrder o ON o.detail_id = d.id WHERE d.type_id = ? GROUP BY d.id) counts ON d.id = counts.d_id ORDER BY buyCount DESC";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setLong(1, type);
        ResultSet resultSet = statement.executeQuery();
        List<Object[]> resultList = new ArrayList<>();
        while (resultSet.next()) {
            resultList.add(new Object[]{resultSet.getString("name"), resultSet.getString("description"), resultSet.getInt("buyCount")});
        }

        List types = DetailType.findAll();
        long type_id = type;
        render(resultList, types, type_id);
    }


    static private String detailsError;

    public static void details() {
        //todo: сделать возможность изменения количества деталей (только добавления, удалять будем при заказах (триггер))!!!
        List types = DetailType.findAll();

        List resultList = Detail.findAll();
        /*Connection conn = DB.getConnection();
        Statement statement = conn.createStatement();
        boolean isResultSet = statement.execute("select * from Detail d_table ORDER BY name");
        ResultSet resultSet = null;
        List<Object[]> resultList = new ArrayList<>();
        if (isResultSet) {
            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                resultList.add(new Object[]{resultSet.getString("name")});
            }
        }*/

        String error = detailsError;
        detailsError = null;
        render(resultList, types, error);
    }

    public static void addDetail(String name, String description, int cost, int count, long type) {
        DetailType detailType = DetailType.findById(type);
        if(detailType == null)
            detailsError = "Please choose a detail type!";
        else {
            if (!"".equals(name) && !"".equals(description)) {
                Detail detail = new Detail(detailType, cost, name, description, count);

                if (!validation.valid(detail).ok) {
                    detailsError = "Impossible to add a detail!";
                } else {
                    detail.save();
                }
            } else
                detailsError = "Impossible to add a detail! Please fill in fields!";
        }
        Application.details();
    }

    public static void deleteDetail(long id) //если есть какие-то связи. Конечно, можно было бы включить cascade, но разумно ли удалять все заказы, в которых встречаются детали?
    {
        try {
            Detail.delete("delete from Detail where id = ?1", id);
        }
        catch(PersistenceException e) {
            detailsError = "Unable to delete a detail which is used in some projects. It should be preserved.";
        }
        details();
    }

    public static void getEmployees(String position)    //может, добавить сортировку (выбор только менеджеров/инженеров)?
    {
        List<Employee> employees = Employee.find("order by salary desc").fetch();

        String error = employeesError;
        System.out.println(error);
        employeesError = null;
        render(employees, error);
    }

    static private String employeesError;


    public static void addEmployee(String firstName, String lastName, String phoneNumber, String email, int salary, double bonusPercent, String position)
    {
        if("manager".equals(position) || "engineer".equals(position))
        {
            if(!"".equals(firstName) && !"".equals(lastName) && !"".equals(email) && !"".equals(phoneNumber)) {
                Employee employee = new Employee(firstName, lastName, position, phoneNumber, email, salary, bonusPercent);

                if (!validation.valid(employee).ok) {
                    employeesError = "Impossible to add an employee!";
                } else {
                    employee.save();
                }
            }
            else
                employeesError = "Please fill in fields!";
        }
        else
            employeesError = "Wrong position!";

        getEmployees(null);
    }

    public static void deleteEmployee(long id) throws SQLException
    {
        Employee employee = Employee.findById(id);
        if(employee == null)
            getEmployees(null);

        Connection conn = DB.getConnection();

        Statement statement = conn.createStatement();
        int c;
        if ("manager".equals(employee.position))
            c = statement.executeUpdate("UPDATE Project SET manager_id = null WHERE manager_id = " + id);
        else
            c = statement.executeUpdate("UPDATE Project SET engineer_id = null WHERE engineer_id = " + id);

        conn.commit();
        Employee.delete("delete from Employee where id = ?1", id);
        if(c > 0)
                employeesError = "Employee was deleted. All projects where they were involved were modified";
        getEmployees(null);
    }

    static private String clientsError;

    public static void clients()
    {
        List resultList = Client.findAll();

        String error = clientsError;
        clientsError = null;
        render(resultList, error);
    }

    public static void deleteClient(long id) throws SQLException
    {
        /*try {
            Client.delete("delete from Client where id = ?1", id);
        }
        catch(PersistenceException e)
        {*/

            Connection conn = DB.getConnection();

            Statement statement = conn.createStatement();
            int c = statement.executeUpdate("UPDATE Project SET client_id = null WHERE client_id = " + id);
            statement.close();

            conn.commit();

            Client.delete("delete from Client where id = ?1", id);
        if(c > 0)
            clientsError = "Client was deleted. All projects where they were involved were modified";

        clients();
    }

    public static void addClient(String firstName, String lastName, String phoneNumber, String email, boolean isPhysical)
    {
        if(!"".equals(firstName) && !"".equals(lastName) && !"".equals(email) && !"".equals(phoneNumber)) {
            Client client = new Client(firstName, lastName, phoneNumber, email, isPhysical);
                if (!validation.valid(client).ok) {
                    clientsError = "Impossible to add an employee! Check your input";
                } else {
                    client.save();
                }
            }
        else
            clientsError = "Please fill in fields!";


        clients();
    }

    public static void findGuaranteedProjects()
    {
        List projects = Project.find("Select p from Project p where isGuaranteed = true").fetch();
        render(projects);
    }

    public static void clientProjects() throws SQLException
    {
        Connection conn = DB.getConnection();
        String query = "SELECT * FROM Client c JOIN (Select c.id as c_id, count(*) as p_count, sum(p.workCost) as workSum, sum(detailCost) as detailSum from Client c JOIN Project p ON p.client_id = c.id group by c.id) counts ON c.id = counts.c_id";
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        List<Object[]> resultList = new ArrayList<>();
        while (resultSet.next()) {
            resultList.add(new Object[]{resultSet.getString("firstName"), resultSet.getString("lastName") ,resultSet.getString("p_count"), resultSet.getLong("workSum") + resultSet.getLong("detailSum")  });
        }

        render(resultList);
    }

    public static void getAssembling() throws SQLException
    {
        Connection conn = DB.getConnection();
        CallableStatement cs = conn.prepareCall("{CALL get_assembling(?, ?)}");
        cs.setString(1, "max");
        cs.registerOutParameter(2, Types.INTEGER);
        cs.execute();
        System.out.println(cs.getInt(2));
    }
}

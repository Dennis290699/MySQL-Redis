package main;

import java.sql.SQLException;
import java.util.List;

import dao.EmployeeDAO;
import model.Employee;

public class Main {
    public static void main(String[] args) {
        EmployeeDAO employeeDAO = null;
        try {
            employeeDAO = new EmployeeDAO();
            List<Employee> employees = employeeDAO.getAllEmployees();
//            for (Employee employee : employees) {
//                System.out.println(employee.getFirstName() + " " + employee.getLastName());
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (employeeDAO != null) {
                employeeDAO.closeConnections();
            }
        }
    }
}
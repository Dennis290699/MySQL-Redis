package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Employee;
import redis.clients.jedis.Jedis;
import util.DatabaseUtil;
import util.RedisUtil;

public class EmployeeDAO {
    private Connection connection;
    private Jedis jedis;

    // Constructor que inicializa la conexion
    public EmployeeDAO() throws SQLException {
        connection = DatabaseUtil.getConnection();
        jedis = RedisUtil.getConnection();
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String cacheKey = "employees:all";
        long startTime, endTime;

        // Intentar obtener los datos de Redis
        startTime = System.currentTimeMillis();
        System.out.println("Buscando datos en Redis...");
        if (jedis.exists(cacheKey)) {
            Map<String, String> cachedData = jedis.hgetAll(cacheKey);
            for (Map.Entry<String, String> entry : cachedData.entrySet()) {
                String[] fields = entry.getValue().split(",");
                Employee employee = new Employee(
                        Integer.parseInt(entry.getKey()),
                        LocalDate.parse(fields[0]),
                        fields[1],
                        fields[2],
                        fields[3],
                        Integer.parseInt(fields[4])
                );
                employee.setDepartmentName(fields[5]);
                employees.add(employee);
            }
            endTime = System.currentTimeMillis();
            System.out.println("Datos obtenidos de Redis. Cantidad de empleados: " + employees.size());
            System.out.println("Tiempo tomado: " + (endTime - startTime) / 1000.0 + " segundos");
            return employees;
        } else {
            System.out.println("Datos no encontrados en Redis.");
        }

        // Si los datos no están en Redis, realizar la consulta a MySQL
        System.out.println("Accediendo a MySQL...");
        startTime = System.currentTimeMillis();
        String sql = "SELECT e.emp_no, e.birth_date, e.first_name, e.last_name, e.gender, s.salary, d.dept_name "
                + "FROM employees e " + "INNER JOIN salaries s ON e.emp_no = s.emp_no "
                + "INNER JOIN dept_emp de ON e.emp_no = de.emp_no "
                + "INNER JOIN departments d ON de.dept_no = d.dept_no";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int empNo = resultSet.getInt(1);
                LocalDate birthDate = resultSet.getDate(2).toLocalDate();
                String firstName = resultSet.getString(3);
                String lastName = resultSet.getString(4);
                String gender = resultSet.getString(5);
                int salary = resultSet.getInt(6);
                String departmentName = resultSet.getString(7);

                Employee employee = new Employee(empNo, birthDate, firstName, lastName, gender, null);
                employee.setSalary(salary);
                employee.setDepartmentName(departmentName);
                employees.add(employee);

                String value = birthDate + "," + firstName + "," + lastName + "," + gender + "," + salary + "," + departmentName;
                jedis.hset(cacheKey, String.valueOf(empNo), value);
            }
        }
        endTime = System.currentTimeMillis();
        System.out.println("Datos obtenidos de MySQL. Cantidad de empleados: " + employees.size());
        System.out.println("Tiempo tomado: " + (endTime - startTime) / 1000.0 + " segundos");

        return employees;
    }

    // Metodo para cerrar la conexion de manera explicita
    public void closeConnections() {
        DatabaseUtil.closeConnection(connection);
        RedisUtil.closeConnection();
    }
}
import models.Department;
import models.Employee;
import services.DepartmentService;
import services.EmployeeService;

import java.io.*;
import java.util.Set;
import java.util.TreeSet;

public class Main {
    public static DepartmentService departmentService = DepartmentService.getInstance();
    public static EmployeeService employeeService = EmployeeService.getInstance();
    public static Set<Employee> employees = new TreeSet<>();

    public static void main(String[] args) throws IOException {

// Создание TreeSet из файла
        createEmployeesBaseFromFile(args[0]);


// Create + Update + Delete
        employeeService.createEmployee(employees, "Яхелева Маргарита Владиславовна", "sup@innotechnum.com", "sup");
        employeeService.updateEmployee(employees, "Яхелева Маргарита Владиславовна", "sup@ts.com", "sup");
        employeeService.deleteEmployee(employees, "Яхелева Маргарита Владиславовна");

// Вывод информации о всех сотрудниках компании
        employeeService.showAllEmployeesInfo(employees);
        System.out.println();

// Вывод всех департаментов компании
        departmentService.showAllDepartments();
        System.out.println();

// Вывод информации о всех сотрудниках департамента "depName"
        String depName = "Департамент развития производства";
        employeeService.showEmployeesByDepartment(depName);
    }

    public static void createEmployeesBaseFromFile (String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;

        while (reader.ready()) {
            line = reader.readLine();

            if (line.length() > 0) {
                String name, email, position;
                Department department;

                name = line.split("#")[0].trim();

                email = line.split("#")[1].trim();
                if (!email.contains("@")) {
                    email = "----------";
                }

                department = departmentService.createNewDepartment(line.split("#")[2].trim());

                position = line.split("#")[3];

                Employee tmpEmp = new Employee(name, email, position);
                employees.add(tmpEmp);
                department.getIncludedEmployees().add(tmpEmp);
            }
        }

        reader.close();
    }
}

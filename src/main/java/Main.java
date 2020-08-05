import models.Department;
import models.Employee;
import services.DepartmentService;
import services.EmployeeService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class Main {
    public static Set<Employee> employees = new TreeSet<>();
    public static DepartmentService departmentService = DepartmentService.getInstance();
    public static EmployeeService employeeService = EmployeeService.getInstance();

    public static void main(String[] args) throws IOException {

// Создание TreeSet из файла
        String fileName = "src/main/resources/Employees.txt";
        createEmployeesBaseFromFile(fileName);


// Create + Update + Delete
//        employeeService.createEmployee(employees, "Яхелева Маргарита Владиславовна", "sup@innotechnum.com", departmentService.createNewDepartment("sup"), "sup");
//        employeeService.updateEmployee(employees, "Яхелева Маргарита Владиславовна", "sup@ts.com", departmentService.createNewDepartment("sup"), "sup");
//        employeeService.deleteEmployee(employees, "Яхелева Маргарита Владиславовна");

// Вывод информации о всех сотрудниках компании
        employeeService.showAllEmployeesInfo(employees);

// Вывод всех департаментов компании
//        employeeService.showAllDepartments(departmentService);

// Вывод информации о всех сотрудниках департамента "depName"
        String depName = "Отдел продвижения";
        employeeService.showEmployeesByDepartment(employees, depName);
    }

    public static void createEmployeesBaseFromFile (String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;

        while ((line = reader.readLine()) != null) {
            String name, email, position;
            Department department = null;

            name = line.split("\t")[0].trim();
            line = line.replaceAll(name + "\t", "");

            email = line.split("\t")[0].trim();
            if (email.contains("@")) {
                line = line.replaceAll(email + "\t", "");
            } else {
                email = "----------";
                line = line.replaceAll("^\t", "");
            }

            department = departmentService.createNewDepartment(line.split("\t")[0]);
            line = line.replaceAll(department.getTitle() + "\t", "");

            position = line;

            employees.add(new Employee(name, email, department, position));
        }
    }
}

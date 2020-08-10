import models.Department;
import models.Employee;
import services.DepartmentService;
import services.EmployeeService;

import java.io.*;
import java.math.BigDecimal;

import static services.EmployeeService.employees;

public class Main {
    public static DepartmentService departmentService = DepartmentService.getInstance();
    public static EmployeeService employeeService = EmployeeService.getInstance();

    public static void main(String[] args) throws IOException {

// Создание TreeSet из файла
        createEmployeesBaseFromFile(args[0]);


// Create + Update + Delete
        employeeService.createEmployee("Яхелева Маргарита Владиславовна", "sup@innotechnum.com", "sup", new BigDecimal("1000"));
        employeeService.updateEmployee("Яхелева Маргарита Владиславовна", "sup@ts.com", "sup", new BigDecimal("2000"));
        employeeService.deleteEmployee("Яхелева Маргарита Владиславовна");

// Вывод информации о всех сотрудниках компании
        employeeService.showAllEmployeesInfo();
        System.out.println();

// Вывод всех департаментов компании
        departmentService.showAllDepartments();
        System.out.println();

// Вывод информации о всех сотрудниках департамента "depName"
        String depName = "Руководство компании";
        employeeService.showEmployeesByDepartment(depName);
        System.out.println();

// Вывод в файл возможных перемещений сотрудника
        String empName = "Одинцов Дмитрий Лукьянович";
        try {
            employeeService.inWhichDepCanEmpMove(empName, args[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createEmployeesBaseFromFile (String fileName) throws FileNotFoundException {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Файл с данными не найден");
        }
        String line;

        try {
            while (reader.ready()) {
                line = reader.readLine();

                if (line.length() > 0) {
                    String name, email, position;
                    Department department;
                    BigDecimal salary;

                    name = line.split("#")[0].trim();

                    email = line.split("#")[1].trim();
                    if (!email.matches("[\\w]*@[a-zA-Z]*\\.(com|ru|net)")) {
                        email = "INVALID EMAIL";
                    }

                    department = departmentService.checkIfDepartmentAlreadyCreated(line.split("#")[2].trim());

                    position = line.split("#")[3];

                    try {
                        salary = new BigDecimal(line.split("#")[4].trim());
                    } catch (NumberFormatException e) {
                        System.out.printf("У работника %s введена некорректная з/п. Он не будет включен в базу.\n", name);
                        continue;
                    }

                    if (salary.compareTo(new BigDecimal("999999")) == 1) {
                        System.out.printf("У работника %s з/п >= 1.000.000, обратите внимание.\n", name);
                    }

                    Employee tmpEmp = new Employee(name, email, position, salary);
                    employees.add(tmpEmp);
                    department.getIncludedEmployees().add(tmpEmp);
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}

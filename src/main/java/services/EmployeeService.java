package services;

import models.Employee;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;

public class EmployeeService {
    private static EmployeeService instance;
    private static DepartmentService departmentService = DepartmentService.getInstance();

    private EmployeeService() {
    }

    public static EmployeeService getInstance() {
        if (instance == null) {
            instance = new EmployeeService();
        }
        return instance;
    }

    public void createEmployee(Set<Employee> employees, String name, String email, String position, BigDecimal salary) {
        employees.add(new Employee(name, email, position, salary));
    }

    public void updateEmployee(Set<Employee> employees, String name, String newEmail, String newPosition, BigDecimal newSalary) {
        for (Employee empl: employees) {
            if (empl.getName().equals(name)) {
                empl.setEmail(newEmail);
                empl.setPosition(newPosition);
                empl.setSalary(newSalary);
            }
        }
    }

    public void deleteEmployee(Set<Employee> employees, String name) {
        for (Employee empl: employees) {
            if (empl.getName().equals(name)) {
                employees.remove(empl);
            }
        }
    }

    public void showAllEmployeesInfo(Set<Employee> employees) {
        for (Employee empl : employees) {
            System.out.println(empl);
        }
    }

    public void showEmployeesByDepartment(String depName) {
        if (departmentService.getDepartments().containsKey(depName)) {
            for (Employee emp : departmentService.getDepartments().get(depName).getIncludedEmployees()) {
                System.out.println(emp);
            }
        }
    }

    public void inWhichDepCanEmpMove(String name, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
                

        writer.close();
    }
}

package services;

import models.*;

import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;

public class EmployeeService {
    private static EmployeeService instance;
    private static DepartmentService departmentService = DepartmentService.getInstance();
    public static Set<Employee> employees = new TreeSet<>();

    private EmployeeService() {
    }

    public static EmployeeService getInstance() {
        if (instance == null) {
            instance = new EmployeeService();
        }
        return instance;
    }

    public void createEmployee(String name, String email, String position, BigDecimal salary) {
        employees.add(new Employee(name, email, position, salary));
    }

    public void updateEmployee(String name, String newEmail, String newPosition, BigDecimal newSalary) {
        for (Employee empl: employees) {
            if (empl.getName().equals(name)) {
                empl.setEmail(newEmail);
                empl.setPosition(newPosition);
                empl.setSalary(newSalary);
            }
        }
    }

    public void deleteEmployee(String name) {
        for (Employee empl: employees) {
            if (empl.getName().equals(name)) {
                employees.remove(empl);
            }
        }
    }

    public void showAllEmployeesInfo() {
        for (Employee empl : employees) {
            System.out.println(empl);
        }
        System.out.println();
    }

    public void showEmployeesByDepartment(String depName) {
        /*if (departmentService.getDepartments().containsKey(depName)) {
            for (Employee emp : departmentService.getDepartments().get(depName).getIncludedEmployees()) {
                System.out.println(emp);
            }
        }*/
        for (Department dep : departmentService.getDepartments().values()) {
            if (dep.getName().equals(depName)) {
                dep.getIncludedEmployees().forEach(System.out::println);
            }
        }
        System.out.println();
    }
}

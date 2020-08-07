package services;

import models.Employee;

import java.util.Set;
import java.util.TreeSet;

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

    public void createEmployee(Set<Employee> employees, String name, String email, String position) {
        employees.add(new Employee(name, email, position));
    }

    public void updateEmployee(Set<Employee> employees, String name, String newEmail, String newPosition) {
        for (Employee empl: employees) {
            if (empl.getName().equals(name)) {
                empl.setEmail(newEmail);
                empl.setPosition(newPosition);
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
}

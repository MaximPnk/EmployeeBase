package services;

import models.Department;
import models.Employee;

import java.util.Set;

public class EmployeeService {
    private static EmployeeService instance;

    private EmployeeService() {
    }

    public static EmployeeService getInstance() {
        if (instance == null) {
            instance = new EmployeeService();
        }
        return instance;
    }

    public void showAllEmployeesInfo(Set<Employee> employees) {
        for (Employee empl : employees) {
            System.out.println(empl);
        }
    }

    public void showAllDepartments(DepartmentService departmentService) {
        for (Department dep : departmentService.getDepartments()) {
            System.out.println(dep);
        }
    }

    public void showEmployeesByDepartment(Set<Employee> employees, String depName) {
        for (Employee emp: employees) {
            if (emp.getDepartment().getTitle().equals(depName)) {
                System.out.println(emp);
            }
        }
    }

    public void createEmployee(Set<Employee> employees, String name, String email, Department department, String position) {
        employees.add(new Employee(name, email, department, position));
    }

    public void updateEmployee(Set<Employee> employees, String name, String newEmail, Department newDepartment, String newPosition) {
        for (Employee empl: employees) {
            if (empl.getName().equals(name)) {
                empl.setEmail(newEmail);
                empl.setDepartment(newDepartment);
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
}

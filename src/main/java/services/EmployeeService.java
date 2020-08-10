package services;

import models.Department;
import models.Employee;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
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
    }

    public void showEmployeesByDepartment(String depName) {
        if (departmentService.getDepartments().containsKey(depName)) {
            for (Employee emp : departmentService.getDepartments().get(depName).getIncludedEmployees()) {
                System.out.println(emp);
            }
        }
    }

    public void inWhichDepCanEmpMove(String name, String fileName) throws IOException {
        Employee soughtEmpl = null;
        Department soughtDep = null;
        String soughtDepName = "";
        boolean isFound = false;
        int count = 0;

        for (Map.Entry<String, Department> dep : departmentService.getDepartments().entrySet()) {
            for (Employee empl : dep.getValue().getIncludedEmployees()) {
                if (empl.getName().equals(name)) {
                    soughtDep = dep.getValue();
                    soughtEmpl = empl;
                    soughtDepName = dep.getKey();
                    isFound = true;
                    break;
                }
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        if (!isFound) {
            writer.write(String.format("Работник %s отсутствует в базе\n", name));
            writer.close();
            return;
        }

        if (soughtDep.getIncludedEmployees().size() < 2) {
            writer.write(String.format("В департаменте %s находится всего один человек\n", soughtDepName));
            writer.close();
            return;
        }

        BigDecimal averageSalaryBeforeMove = soughtDep.averageDepartmentSalary();
        soughtDep.getIncludedEmployees().remove(soughtEmpl);
        BigDecimal averageSalaryAfterMove = soughtDep.averageDepartmentSalary();
        soughtDep.getIncludedEmployees().add(soughtEmpl);
        BigDecimal difference = averageSalaryAfterMove.subtract(averageSalaryBeforeMove);

        if (!difference.abs().equals(difference)) {
            writer.write(String.format("Сотрудника %s переместить нельзя. В исходном департаменте средняя з/п упадет с %s до %s\n", soughtEmpl.getName(), averageSalaryBeforeMove, averageSalaryAfterMove));
            writer.close();
            return;
        } else {
            writer.write(String.format("Сотрудника %s можно переместить. В исходном департаменте средняя з/п увеличится с %s до %s\n", soughtEmpl.getName(), averageSalaryBeforeMove, averageSalaryAfterMove));
        }

        for (Map.Entry<String, Department> dep : departmentService.getDepartments().entrySet()) {
            if (dep.getValue() == soughtDep) {
                continue;
            }
            BigDecimal avSalBefore = dep.getValue().averageDepartmentSalary();
            dep.getValue().getIncludedEmployees().add(soughtEmpl);
            BigDecimal avSalAfter = dep.getValue().averageDepartmentSalary();
            dep.getValue().getIncludedEmployees().remove(soughtEmpl);
            BigDecimal dif = avSalAfter.subtract(avSalBefore);
            if (dif.abs().equals(dif)) {
                count++;
                writer.append(String.format("Можно переместить в %s. Средняя з/п вырастет с %s до %s\n", dep.getKey(), avSalBefore, avSalAfter));
            }
        }

        if (count == 0) {
            writer.append("Во всех департаментах з/п уменьшится. Переместить не получится.\n");
        }

        writer.close();
    }

    public static Set<Employee> getEmployees() {
        return employees;
    }
}

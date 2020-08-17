package io;

import models.*;
import services.DepartmentService;

import java.io.*;
import java.math.BigDecimal;

import static services.EmployeeService.employees;

public class CreateBaseFromFile {
    public static DepartmentService departmentService = DepartmentService.getInstance();

    public static void readFromFile (String fileName) {
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while (reader.ready()) {
                line = reader.readLine();
                checkCorrectLine(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл с данными не найден");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private static void checkCorrectLine(String line) {

        if (!(line.split("#").length == 5 && line.matches(".*[a-zA-Zа-яА-Я]+.*#.*#.*[a-zA-Zа-яА-Я]+.*#.*[a-zA-Zа-яА-Я]+.*#.*"))) {
            if (!line.isEmpty()) {
                System.out.println("Данная строка не корректна: " + line);
            }
            return;
        }

        String name, email, position;
        String department;
        BigDecimal salary;

        name = line.split("#")[0].trim();

        email = line.split("#")[1].trim();
        if (!email.matches("[\\w]*@[a-zA-Z]*\\.(com|ru|net)")) {
            email = "INVALID EMAIL";
        }

        department = line.split("#")[2].trim();

        position = line.split("#")[3].trim();

        try {
            salary = new BigDecimal(line.split("#")[4].trim());
        } catch (NumberFormatException e) {
            System.out.printf("У работника %s введена некорректная з/п. Он не будет включен в базу.\n", name);
            return;
        }

        if (salary.compareTo(new BigDecimal("999999")) > 0) {
            System.out.printf("У работника %s з/п >= 1.000.000, обратите внимание.\n", name);
        } else if (salary.compareTo(new BigDecimal("0")) <= 0 || salary.scale() > 2) {
            System.out.printf("У работника %s з/п = %s. Он не будет включен в базу.\n", name, salary);
            return;
        }

        addNewEmplAndDep(department, name, email, position, salary);
    }

    public static void addNewEmplAndDep(String dep, String name, String email, String position, BigDecimal salary) {
        Department department = departmentService.checkIfDepartmentAlreadyCreated(dep);
        Employee tmpEmp = new Employee(name, email, position, salary);
        employees.add(tmpEmp);
        department.getIncludedEmployees().add(tmpEmp);
    }
}

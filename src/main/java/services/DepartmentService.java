package services;

import models.Department;

import java.util.HashMap;
import java.util.Map;

public class DepartmentService {
    private static DepartmentService instance;
    private HashMap<String, Department> departments = new HashMap();

    private DepartmentService() {
    }

    public static DepartmentService getInstance() {
        if (instance == null) {
            instance = new DepartmentService();
        }
        return instance;
    }

//    Создание списка департаментов
    public Department checkIfDepartmentAlreadyCreated(String title) {
        Department department = departments.getOrDefault(title, new Department(title));
        departments.putIfAbsent(title, department);
        return department;
    }

    public void showAllDepartments() {
        for (Department dep : departments.values()) {
            System.out.printf("%-40sСредняя з/п = %s\n", dep.getName(), dep.averageDepartmentSalary());
        }
        System.out.println();
    }

    public Map<String, Department> getDepartments() {
        return departments;
    }
}

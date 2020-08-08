package services;

import models.Department;

import java.util.HashMap;
import java.util.Map;

public class DepartmentService {
    private static DepartmentService instance;
    private Map<String, Department> departments = new HashMap();

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
        if (departments.get(title) != null) {
            return departments.get(title);
        }
        Department department = new Department();
        departments.put(title , department);
        return department;
    }

    public void showAllDepartments() {
        for (Map.Entry<String, Department> pair : departments.entrySet()) {
            System.out.printf("%-40sСредняя з/п = %s\n", pair.getKey(), pair.getValue().averageDepartmentSalary());
        }
    }

    public Map<String, Department> getDepartments() {
        return departments;
    }

    public void setDepartments(Map<String, Department> departments) {
        this.departments = departments;
    }
}

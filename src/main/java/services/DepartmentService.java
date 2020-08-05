package services;

import models.Department;

import java.util.HashSet;
import java.util.Set;

public class DepartmentService {
    private static DepartmentService instance;
    private Set<Department> departments = new HashSet<>();
    private Department department;

    private DepartmentService() {
    }

    public static DepartmentService getInstance() {
        if (instance == null) {
            instance = new DepartmentService();
        }
        return instance;
    }

//    Создание списка департаментов
    public Department createNewDepartment(String title) {
        for (Department dep: departments) {
            if (dep.getTitle().equals(title)) {
                return dep;
            }
        }
        department = new Department(title);
        departments.add(department);
        return department;
    }

    public Set<Department> getDepartments() {
        return departments;
    }
}

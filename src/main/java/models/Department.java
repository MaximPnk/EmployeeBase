package models;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private List<Employee> includedEmployees = new ArrayList<>();

    public List<Employee> getIncludedEmployees() {
        return includedEmployees;
    }
}

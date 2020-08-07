package models;

import java.util.Set;
import java.util.TreeSet;

public class Department {
    private Set<Employee> includedEmployees = new TreeSet<>();

    public Set<Employee> getIncludedEmployees() {
        return includedEmployees;
    }
}

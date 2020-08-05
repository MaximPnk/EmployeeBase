package models;

import java.util.Objects;

public class Employee implements Comparable {
    private String name;
    private String email;
    private Department department;
    private String position;

    public Employee(String name, String email, Department department, String position) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public int compareTo(Object o) {
        return this.name.compareTo(((Employee) o).name);
    }

    @Override
    public String toString() {
        return String.format("%-40s%-40s%-40s%s", name, email, department, position);
    }

}

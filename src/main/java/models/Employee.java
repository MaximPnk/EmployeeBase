package models;

import java.math.BigDecimal;

public class Employee implements Comparable {
    private String name;
    private String email;
    private String position;
    private BigDecimal salary;

    public Employee(String name, String email, String position, BigDecimal salary) {
        this.name = name;
        this.email = email;
        this.position = position;
        this.salary = salary;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @Override
    public int compareTo(Object o) {
        return this.name.compareTo(((Employee) o).name);
    }

    @Override
    public String toString() {
        return String.format("%-40s%-40s%-40s%s", name, email, position, salary);
    }

}

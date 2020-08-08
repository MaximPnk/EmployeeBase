package models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.TreeSet;

public class Department {
    private Set<Employee> includedEmployees = new TreeSet<>();

    public BigDecimal averageDepartmentSalary() {
        BigDecimal average, count = new BigDecimal("0"), summarySalary = new BigDecimal("0");
            for (Employee empl : includedEmployees) {
                summarySalary = summarySalary.add(empl.getSalary());
                count = count.add(BigDecimal.valueOf(1));
            }
            average = summarySalary.divide(count, 2, RoundingMode.CEILING);
        return average;
    }

    public Set<Employee> getIncludedEmployees() {
        return includedEmployees;
    }
}

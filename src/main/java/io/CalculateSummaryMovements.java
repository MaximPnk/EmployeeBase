package io;

import models.Department;
import models.Employee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

public class CalculateSummaryMovements {
    public final BigDecimal salaryBeforeMoveInStartDep;
    public final BigDecimal salaryAfterMoveInStartDep;
    public final BigDecimal salaryBeforeMoveInFinishDep;
    public final BigDecimal salaryAfterMoveInFinishDep;
    private static BigDecimal sumSalary, size, average;

    private CalculateSummaryMovements(BigDecimal salaryBeforeMoveInStartDep, BigDecimal salaryAfterMoveInStartDep, BigDecimal salaryBeforeMoveInFinishDep, BigDecimal salaryAfterMoveInFinishDep) {
        this.salaryBeforeMoveInStartDep = salaryBeforeMoveInStartDep;
        this.salaryAfterMoveInStartDep = salaryAfterMoveInStartDep;
        this.salaryBeforeMoveInFinishDep = salaryBeforeMoveInFinishDep;
        this.salaryAfterMoveInFinishDep = salaryAfterMoveInFinishDep;
    }

    public static boolean checkIfMovable(Department startDep, Department finishDep, List<Employee> empsList, List<String> binaryList) {
        sumSalary = new BigDecimal(0);
        size = new BigDecimal(0);
        average = new BigDecimal(0);
        for (int i = binaryList.size() - 1, j = empsList.size() - 1; i >= 0; i--, j--) {
            if (binaryList.get(i).equals("1")) {
                sumSalary = sumSalary.add(empsList.get(j).getSalary());
            }
        }
        size = new BigDecimal(Collections.frequency(binaryList, "1"));
        average = sumSalary.divide(size, 2, RoundingMode.HALF_UP);
        return average.compareTo(startDep.averageDepartmentSalary()) < 0 && average.compareTo(finishDep.averageDepartmentSalary()) > 0;
    }

    public static CalculateSummaryMovements calculate(Department startDep, Department finishDep) {
        BigDecimal salaryBeforeMoveInStartDep = startDep.averageDepartmentSalary();
        BigDecimal salaryAfterMoveInStartDep;
        BigDecimal salaryBeforeMoveInFinishDep = finishDep.averageDepartmentSalary();
        BigDecimal salaryAfterMoveInFinishDep;

        salaryAfterMoveInStartDep = startDep.averageDepartmentSalary()
                .multiply(new BigDecimal(startDep.getIncludedEmployees().size()))
                .subtract(sumSalary)
                .divide(new BigDecimal((startDep.getIncludedEmployees().size())).subtract(size), 2, RoundingMode.HALF_UP);
        salaryAfterMoveInFinishDep = finishDep.averageDepartmentSalary()
                .multiply(new BigDecimal(finishDep.getIncludedEmployees().size()))
                .add(sumSalary)
                .divide(new BigDecimal(finishDep.getIncludedEmployees().size()).add(size), 2, RoundingMode.HALF_UP);

        return new CalculateSummaryMovements(salaryBeforeMoveInStartDep, salaryAfterMoveInStartDep, salaryBeforeMoveInFinishDep, salaryAfterMoveInFinishDep);
    }
}

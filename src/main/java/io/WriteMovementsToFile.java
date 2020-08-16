package io;

import models.Department;
import models.Employee;
import services.DepartmentService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;

public class WriteMovementsToFile {
    public static DepartmentService departmentService = DepartmentService.getInstance();
    private static String file;

    public static void whereEmpCanMove(String fileName) {
        file = fileName;
        clear();

        BigDecimal salaryBeforeMoveInStartDep;
        BigDecimal salaryAfterMoveInStartDep;
        BigDecimal differenceInStartDep;
        BigDecimal salaryBeforeMoveInFinishDep;
        BigDecimal salaryAfterMoveInFinishDep;
        BigDecimal differenceInFinishDep;

        for (Department startDep : departmentService.getDepartments().values()) {

            if (startDep.getIncludedEmployees().size() < 2) {
                continue;
            }

            for (Employee empl : new TreeSet<>(startDep.getIncludedEmployees())) {

                salaryBeforeMoveInStartDep = startDep.averageDepartmentSalary();
                startDep.getIncludedEmployees().remove(empl);
                salaryAfterMoveInStartDep = startDep.averageDepartmentSalary();
                startDep.getIncludedEmployees().add(empl);
                differenceInStartDep = salaryAfterMoveInStartDep.subtract(salaryBeforeMoveInStartDep);

                if (differenceInStartDep.compareTo(new BigDecimal("0")) <= 0) {
                    continue;
                }

                for (Department finishDep : departmentService.getDepartments().values()) {

                    if (startDep == finishDep) {
                        continue;
                    }

                    salaryBeforeMoveInFinishDep = finishDep.averageDepartmentSalary();
                    finishDep.getIncludedEmployees().add(empl);
                    salaryAfterMoveInFinishDep = finishDep.averageDepartmentSalary();
                    finishDep.getIncludedEmployees().remove(empl);
                    differenceInFinishDep = salaryAfterMoveInFinishDep.subtract(salaryBeforeMoveInFinishDep);

                    if (differenceInFinishDep.compareTo(new BigDecimal("0")) <= 0) {
                        continue;
                    }

                    writeMove(String.format("%s можно переместить из %s в %s. З/п вырастет с %s до %s и с %s до %s соответственно\n", empl.getName(), startDep.getName(), finishDep.getName(), salaryBeforeMoveInStartDep, salaryAfterMoveInStartDep, salaryBeforeMoveInFinishDep, salaryAfterMoveInFinishDep));
                }
            }
        }
    }

    private static void writeMove(String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clear() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

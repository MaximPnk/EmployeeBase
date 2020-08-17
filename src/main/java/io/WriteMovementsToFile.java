package io;

import models.*;
import services.DepartmentService;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class WriteMovementsToFile {
    public static DepartmentService departmentService = DepartmentService.getInstance();

    public static void whereEmpCanMove(String fileName) {

        BigDecimal salaryBeforeMoveInStartDep;
        BigDecimal salaryAfterMoveInStartDep;
        BigDecimal salaryBeforeMoveInFinishDep;
        BigDecimal salaryAfterMoveInFinishDep;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("");
            for (Department startDep : departmentService.getDepartments().values()) {

                if (startDep.getIncludedEmployees().size() < 2) {
                    continue;
                }

                for (Employee empl : startDep.getIncludedEmployees()) {

                    if (empl.getSalary().compareTo(startDep.averageDepartmentSalary()) >= 0) {
                        continue;
                    }

                    salaryBeforeMoveInStartDep = startDep.averageDepartmentSalary();
                    salaryAfterMoveInStartDep = salaryBeforeMoveInStartDep.multiply(new BigDecimal(startDep.getIncludedEmployees().size())).subtract(empl.getSalary()).divide(new BigDecimal(String.valueOf(startDep.getIncludedEmployees().size()-1)), 2, RoundingMode.HALF_UP);

                    for (Department finishDep : departmentService.getDepartments().values()) {

                        if (startDep == finishDep || empl.getSalary().compareTo(finishDep.averageDepartmentSalary()) <= 0) {
                            continue;
                        }

                        salaryBeforeMoveInFinishDep = finishDep.averageDepartmentSalary();
                        salaryAfterMoveInFinishDep = salaryBeforeMoveInFinishDep.multiply(new BigDecimal(finishDep.getIncludedEmployees().size())).add(empl.getSalary()).divide(new BigDecimal(String.valueOf(finishDep.getIncludedEmployees().size()+1)), 2, RoundingMode.HALF_UP);

                        writeMove(writer, empl.getName(), startDep.getName(), finishDep.getName(), salaryBeforeMoveInStartDep, salaryAfterMoveInStartDep, salaryBeforeMoveInFinishDep, salaryAfterMoveInFinishDep);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeMove(BufferedWriter writer, String emplName, String startDepName, String finishDepName, BigDecimal salaryBeforeInStart, BigDecimal salaryAfterInStart, BigDecimal salaryBeforeInFinish, BigDecimal salaryAfterInFinish) throws IOException {
        writer.append(String.format("%s можно переместить из %s в %s. З/п вырастет с %s до %s и с %s до %s соответственно\n", emplName, startDepName, finishDepName, salaryBeforeInStart, salaryAfterInStart, salaryBeforeInFinish, salaryAfterInFinish));
    }
}

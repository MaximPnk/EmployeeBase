package io;

import models.Department;
import models.Employee;
import services.DepartmentService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class WriteSummaryMovementsToFile {
    static DepartmentService departmentService = DepartmentService.getInstance();

    public static void summaryMovements(String fileName) {
        BufferedWriter writer = openWriter(fileName);
        Date start = new Date();
        for (Department startDep : departmentService.getDepartments().values()) {
            for (Department finishDep : departmentService.getDepartments().values()) {
                if (finishDep == startDep || startDep.getIncludedEmployees().size() < 2) {
                    continue;
                }
                List<Employee> empsList = new ArrayList<>(startDep.getIncludedEmployees());
                int empsQuantity = startDep.getIncludedEmployees().size();
                int binaryLength = (int) Math.pow(2, empsQuantity) - 1;
                recursion(startDep, finishDep, binaryLength, empsList, writer);
            }
        }
        closeWriter(writer);
        System.out.println("Программа отработала за " + (new Date().getTime() - start.getTime()) + " миллисекунд.");
    }

    private static void recursion(Department startDep, Department finishDep, int binaryLength, List<Employee> empsList, BufferedWriter writer) {
        if (binaryLength == 0) {
            return;
        }

        List<String> binaryList = new ArrayList<>(Arrays.asList(Integer.toBinaryString(binaryLength).split("")));
        while (binaryList.size() < empsList.size()) {
            binaryList.add(0, "0");
        }
        if (!binaryList.contains("0")) {
            recursion(startDep, finishDep, --binaryLength, empsList, writer);
            return;
        }

        BigDecimal salaryBeforeMoveInStartDep = startDep.averageDepartmentSalary();
        BigDecimal salaryAfterMoveInStartDep = salaryBeforeMoveInStartDep;
        for (int i = 0; i < empsList.size(); i++) {
            if (binaryList.get(i).equals("1")) {
                salaryAfterMoveInStartDep = salaryAfterMoveInStartDep.multiply(new BigDecimal(startDep.getIncludedEmployees().size())).subtract(empsList.get(i).getSalary()).divide(new BigDecimal(String.valueOf(startDep.getIncludedEmployees().size()-1)), 2, RoundingMode.HALF_UP);
            }
        }

        if (salaryAfterMoveInStartDep.compareTo(salaryBeforeMoveInStartDep) <= 0) {
            recursion(startDep, finishDep, --binaryLength, empsList, writer);
            return;
        }
        BigDecimal salaryBeforeMoveInFinishDep = finishDep.averageDepartmentSalary();
        BigDecimal salaryAfterMoveInFinishDep = salaryBeforeMoveInFinishDep;
        for (int i = 0; i < empsList.size(); i++) {
            if (binaryList.get(i).equals("1")) {
                salaryAfterMoveInFinishDep = salaryAfterMoveInFinishDep.multiply(new BigDecimal(finishDep.getIncludedEmployees().size())).add(empsList.get(i).getSalary()).divide(new BigDecimal(String.valueOf(finishDep.getIncludedEmployees().size()+1)), 2, RoundingMode.HALF_UP);
            }
        }
        if (salaryAfterMoveInFinishDep.compareTo(salaryBeforeMoveInFinishDep) <= 0) {
            recursion(startDep, finishDep, --binaryLength, empsList, writer);
            return;
        }

        List<String> resultEmpsList = new ArrayList<>();
        write(writer, String.format("Откуда: %s. З/п перед = %s, з/п после = %s\n", startDep.getName(), salaryBeforeMoveInStartDep, salaryAfterMoveInStartDep));
        write(writer, String.format("Куда: %s. З/п перед = %s, з/п после = %s\n", finishDep.getName(), salaryBeforeMoveInFinishDep, salaryAfterMoveInFinishDep));
        write(writer, "Сотрудники: ");
        for (int i = 0; i < empsList.size(); i++) {
            if (binaryList.get(i).equals("1")) {
                resultEmpsList.add(empsList.get(i).getName());
            }
        }
        write(writer, String.join(", ", resultEmpsList) + System.lineSeparator() + System.lineSeparator());

        recursion(startDep, finishDep, --binaryLength, empsList, writer);
    }

    private static BufferedWriter openWriter(String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write("");
            return writer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void closeWriter(BufferedWriter writer) {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void write(BufferedWriter writer, String text) {
        try {
            writer.append(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

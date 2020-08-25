package io;

import models.Department;
import models.Employee;
import services.DepartmentService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class WriteSummaryMovementsToFile {
    static DepartmentService departmentService = DepartmentService.getInstance();

    public static void summaryMovements(String fileName) {
        if (departmentService.getDepartments().size() < 2) {
            System.out.println("Невозможен перевод сотрудников при наличии менее двух департаментов");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Department startDep : departmentService.getDepartments().values()) {
                for (Department finishDep : departmentService.getDepartments().values()) {
                    if (finishDep == startDep || startDep.getIncludedEmployees().size() < 2) {
                        continue;
                    }
                    List<Employee> empsList = new ArrayList<>(startDep.getIncludedEmployees());
                    int empsQuantity = startDep.getIncludedEmployees().size();
                    int binaryLength = (int) Math.pow(2, empsQuantity) - 2;
                    binaryAlgorithm(startDep, finishDep, binaryLength, empsList, writer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void binaryAlgorithm(Department startDep, Department finishDep, int binaryLength, List<Employee> empsList, BufferedWriter writer) {
        while (binaryLength > 0) {
            List<String> binaryList = new ArrayList<>(Arrays.asList(Integer.toBinaryString(binaryLength).split("")));

            if (!CalculateSummaryMovements.checkIfMovable(startDep, finishDep, empsList, binaryList)) {
                binaryLength--;
                continue;
            }
            CalculateSummaryMovements calculate = CalculateSummaryMovements.calculate(startDep, finishDep);

            List<String> resultEmpsList = new ArrayList<>();
            write(writer, String.format("Откуда: %s. З/п перед = %s, з/п после = %s\n", startDep.getName(), calculate.salaryBeforeMoveInStartDep, calculate.salaryAfterMoveInStartDep));
            write(writer, String.format("Куда: %s. З/п перед = %s, з/п после = %s\n", finishDep.getName(), calculate.salaryBeforeMoveInFinishDep, calculate.salaryAfterMoveInFinishDep));
            write(writer, "Сотрудники: ");
            for (int i = binaryList.size() - 1, j = empsList.size() - 1; i >= 0; i--, j--) {
                if (binaryList.get(i).equals("1")) {
                    resultEmpsList.add(empsList.get(j).getName());
                }
            }
            write(writer, String.join(", ", resultEmpsList) + System.lineSeparator() + System.lineSeparator());

            binaryLength--;
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

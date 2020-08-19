package io;

import models.*;
import services.DepartmentService;
import services.EmployeeService;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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

    //перебор 2^n-1
    public static void whileAlgorithm(int num, String fileName) {
        //получаем множество 001 010 011 100 101 110 111
        //если элемент i-ый элемент = 1, то пробуем ему поменять департамент
        Date start = new Date();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));) {
            writer.write("");

            Employee[] emps = EmployeeService.employees.toArray(new Employee[EmployeeService.employees.size()]);
            String[] movableEmps = new String[EmployeeService.employees.size()];
            Arrays.fill(movableEmps, "0");
            BigDecimal salaryBeforeMoveInStartDep;
            BigDecimal salaryAfterMoveInStartDep;
            BigDecimal salaryBeforeMoveInFinishDep;
            BigDecimal salaryAfterMoveInFinishDep;
            Department startDep = null;
            Set<String[]> arrays = new HashSet<>();

            //ставим 1 только тем, кто вообще может переместиться
            outer:
            for (int i = 0; i < emps.length; i++) {
                for (Department department : departmentService.getDepartments().values()) {
                    if (department.getIncludedEmployees().contains(emps[i])) {
                        startDep = department;
                        break;
                    }
                }
                if (emps[i].getSalary().compareTo(startDep.averageDepartmentSalary()) >= 0) {
                    continue;
                }
                for (Department finishDep : departmentService.getDepartments().values()) {
                    if (!(startDep == finishDep || emps[i].getSalary().compareTo(finishDep.averageDepartmentSalary()) <= 0)) {
                        movableEmps[i] = "1";
                        continue outer;
                    }
                }
            }

            toWhile:
            while (num != 0) {
                int count = 0;

                //если массив 11..11, то ставим 0 тем, кто не может переместиться совсем
                String[] tmpArray = Integer.toBinaryString(num).split("");
                String[] array = new String[20];
                Arrays.fill(array, "0");
                for (int i = tmpArray.length - 1, j = array.length - 1; i >= 0; i--, j--) {
                    array[j] = tmpArray[i];
                }

                for (int i = array.length - 1, j = movableEmps.length - 1; i >= 0; i--, j--) {
                    if (!movableEmps[j].equals("1")) {
                        array[i] = "0";
                    }
                }

                if (arrays.isEmpty()) {
                    arrays.add(array);
                }
                for (String[] arr : arrays) {
                    if (arr != array && Arrays.equals(arr, array)) {
                        num--;
                        continue toWhile;
                    }
                }
                arrays.add(array);

                for (int i = array.length - 1, j = emps.length - 1; i > 0; i--, j--) {
                    if (array[i].equals("0")) {
                        continue;
                    }

                    for (Department department : departmentService.getDepartments().values()) {
                        if (department.getIncludedEmployees().contains(emps[j])) {
                            startDep = department;
                            break;
                        }
                    }

                    if (emps[j].getSalary().compareTo(startDep.averageDepartmentSalary()) >= 0) {
                        continue;
                    }

                    salaryBeforeMoveInStartDep = startDep.averageDepartmentSalary();
                    salaryAfterMoveInStartDep = salaryBeforeMoveInStartDep.multiply(new BigDecimal(startDep.getIncludedEmployees().size())).subtract(emps[j].getSalary()).divide(new BigDecimal(String.valueOf(startDep.getIncludedEmployees().size() - 1)), 2, RoundingMode.HALF_UP);

                    for (Department finishDep : departmentService.getDepartments().values()) {

                        if (startDep == finishDep || emps[j].getSalary().compareTo(finishDep.averageDepartmentSalary()) <= 0) {
                            continue;
                        }

                        salaryBeforeMoveInFinishDep = finishDep.averageDepartmentSalary();
                        salaryAfterMoveInFinishDep = salaryBeforeMoveInFinishDep.multiply(new BigDecimal(finishDep.getIncludedEmployees().size())).add(emps[j].getSalary()).divide(new BigDecimal(String.valueOf(finishDep.getIncludedEmployees().size() + 1)), 2, RoundingMode.HALF_UP);

                        count++;
                        writeMove(writer, emps[j].getName(), startDep.getName(), finishDep.getName(), salaryBeforeMoveInStartDep, salaryAfterMoveInStartDep, salaryBeforeMoveInFinishDep, salaryAfterMoveInFinishDep);
                    }
                }
                if (count != 0) {
                    writer.append(System.lineSeparator());
                }
                num--;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Время работы цикла while с пробегом по 2^n-1 элементам: " + (new Date().getTime() - start.getTime()));
    }

    //вариант без пробегания 2^n-1 раз. достаточно листа с вложенным листом и в нем варианты
    //рабочий метод через рекурсию
    public static void recursionAlgorithm(String fileName) {
        Date start = new Date();

        BigDecimal salaryBeforeMoveInStartDep;
        BigDecimal salaryAfterMoveInStartDep;
        BigDecimal salaryBeforeMoveInFinishDep;
        BigDecimal salaryAfterMoveInFinishDep;

        ArrayList<ArrayList<String>> outerList = new ArrayList<>();

        for (Department startDep : departmentService.getDepartments().values()) {

            if (startDep.getIncludedEmployees().size() < 2) {
                continue;
            }

            for (Employee empl : startDep.getIncludedEmployees()) {

                ArrayList<String> innerList = new ArrayList<>();

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

                    innerList.add(String.format("%s перемещается из %s в %s. З/п вырастет с %s до %s и с %s до %s соответственно", empl.getName(), startDep.getName(), finishDep.getName(), salaryBeforeMoveInStartDep, salaryAfterMoveInStartDep, salaryBeforeMoveInFinishDep, salaryAfterMoveInFinishDep));
                }

                if (innerList.size() != 0) {
                    outerList.add(innerList);
                }
            }
        }
        int binSize = (int) Math.pow(2, outerList.size()) - 1;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));) {
            writer.write("");
            recursion(outerList, binSize, writer, start);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void recursion(ArrayList<ArrayList<String>> list, int binSize, BufferedWriter writer, Date start) throws IOException {
        if (binSize == 0) {
            writer.close();
            timeOfRecursion(start);
            return;
        }

        String[] array = Integer.toBinaryString(binSize).split("");
        for (int i = array.length-1, j = list.size()-1; i >= 0; i--, j--) {
            if (array[i].equals("1")) {
                for (int k = 0; k < list.get(j).size(); k++) {
                    writer.append(list.get(j).get(k) + System.lineSeparator());
                }
            }
        }
        writer.append(System.lineSeparator());
        recursion(list, --binSize, writer, start);
    }

    private static void timeOfRecursion(Date start) {
        System.out.println("Время работы рекурсивного метода с пробегом только реальных переводов: " + (new Date().getTime() - start.getTime()));
    }











    //список работников и список департаментов (слишком много вариантов)
    //не рабочий
    private static void twoLists() {
        ArrayList<Employee> empList = new ArrayList<>(EmployeeService.employees);
        ArrayList<Department> departments = new ArrayList<>(departmentService.getDepartments().values());
        //делаем лист департаментов = листу сотрудников
        //присваиваем листу департаментов все возможные комбинации департаментов
        //проверяем
        //слишком много комбинаций
    }

    //неправильная логика
    //не рабочий
    private static void WrongMethod() {
        //изменяем порядок в списке эмплоев
        //не получится, не обязательно иметь такое же кол-во сотрудников, которое было изначально
        ArrayList<Employee> list = new ArrayList<>(EmployeeService.employees); //стартовый лист, который содержит постоянные департаменты в ячейках
        ArrayList<Employee> newList = WrongMethod2(list); //лист, полученный после перебора //TODO

        Department startDep = null, finishDep = null;

        BigDecimal salaryBeforeMoveInStartDep;
        BigDecimal salaryAfterMoveInStartDep;
        BigDecimal salaryBeforeMoveInFinishDep;
        BigDecimal salaryAfterMoveInFinishDep;

        for (int i = 0; i < newList.size(); i++) {
            for (Department dep : departmentService.getDepartments().values()) {
                if (dep.getIncludedEmployees().contains(list.get(i))) {
                    finishDep = dep;
                }
                if (dep.getIncludedEmployees().contains(newList.get(i))) {
                    startDep = dep;
                }
                if (startDep != finishDep) {
                    //проверяем, есть ли хоть одно уменьшение з/п в текущем переборе
                    if (newList.get(i).getSalary().compareTo(startDep.averageDepartmentSalary()) > 0 || newList.get(i).getSalary().compareTo(finishDep.averageDepartmentSalary()) < 0) {
                        System.exit(0); //временно
                    }
                }
            }
        }

        for (int i = 0; i < newList.size(); i++) {
            for (Department dep : departmentService.getDepartments().values()) {
                if (dep.getIncludedEmployees().contains(list.get(i))) {
                    finishDep = dep;
                }
                if (dep.getIncludedEmployees().contains(newList.get(i))) {
                    startDep = dep;
                }

                if (newList.get(i).getSalary().compareTo(startDep.averageDepartmentSalary()) >= 0 || startDep == finishDep || newList.get(i).getSalary().compareTo(finishDep.averageDepartmentSalary()) <= 0) {
                    continue;
                }

                salaryBeforeMoveInStartDep = startDep.averageDepartmentSalary();
                salaryAfterMoveInStartDep = salaryBeforeMoveInStartDep.multiply(new BigDecimal(startDep.getIncludedEmployees().size())).subtract(newList.get(i).getSalary()).divide(new BigDecimal(String.valueOf(startDep.getIncludedEmployees().size()-1)), 2, RoundingMode.HALF_UP);
                salaryBeforeMoveInFinishDep = finishDep.averageDepartmentSalary();
                salaryAfterMoveInFinishDep = salaryBeforeMoveInFinishDep.multiply(new BigDecimal(finishDep.getIncludedEmployees().size())).add(newList.get(i).getSalary()).divide(new BigDecimal(String.valueOf(finishDep.getIncludedEmployees().size()+1)), 2, RoundingMode.HALF_UP);

                System.out.printf("%s можно переместить из %s в %s. З/п вырастет с %s до %s и с %s до %s соответственно\n", newList.get(i).getName(), startDep.getName(), finishDep.getName(), salaryBeforeMoveInStartDep, salaryAfterMoveInStartDep, salaryBeforeMoveInFinishDep, salaryAfterMoveInFinishDep);

            }
        }
    }
    private static ArrayList<Employee> WrongMethod2(ArrayList<Employee> list) {
        return list;
    }

    private int foo (int a) {
        if (a == 0) {
            return a;
        }
        a--;
        return foo(a);
    }
}

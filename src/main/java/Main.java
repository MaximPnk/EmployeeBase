import io.*;
import services.*;

public class Main {
    public static DepartmentService departmentService = DepartmentService.getInstance();
    public static EmployeeService employeeService = EmployeeService.getInstance();

    public static void main(String[] args) {

        if (checkArgsLength(args)) {
            CreateBaseFromFile.readFromFile(args[0]);
            employeeService.showAllEmployeesInfo();
            departmentService.showAllDepartments();
            String depName = "Отдел продвижения";
            employeeService.showEmployeesByDepartment(depName);
            WriteMovementsToFile.whereEmpCanMove(args[1]);
            //2 рабочих метода. Первый перебирает 2^n-1 вариантов с помощью цикла. Второй перебирает только активные через рекурсию.
            WriteMovementsToFile.whileAlgorithm((int) Math.pow(2, EmployeeService.employees.size()) - 1, args[1]);
            WriteMovementsToFile.recursionAlgorithm(args[1]);
        }
    }

    public static boolean checkArgsLength(String[] args) {
        if (args.length != 2) {
            System.out.println("Запуск программы происходит со следующими параметрами: \"ИМЯ_ФАЙЛА_ДЛЯ_ЧТЕНИЯ\" \"ИМЯ_ФАЙЛА_ДЛЯ_ВЫВОДА\"");
            return false;
        }
        return true;
    }
}

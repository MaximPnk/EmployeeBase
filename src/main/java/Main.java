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
            WriteSummaryMovementsToFile.summaryMovements(args[1]);
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

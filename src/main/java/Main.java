import io.CreateBaseFromFile;
import io.WriteMovementsToFile;
import services.DepartmentService;
import services.EmployeeService;

import java.io.*;

public class Main {
    public static DepartmentService departmentService = DepartmentService.getInstance();
    public static EmployeeService employeeService = EmployeeService.getInstance();

    public static void main(String[] args) {

        checkArgsLength(args);

        CreateBaseFromFile.readFromFile(args[0]);

        employeeService.showAllEmployeesInfo();

        departmentService.showAllDepartments();

        String depName = "Руководство компании";
        employeeService.showEmployeesByDepartment(depName);

        WriteMovementsToFile.whereEmpCanMove(args[1]);
    }

    public static void checkArgsLength(String[] args) {
        if (args.length != 2) {
            System.out.println("Запуск программы происходит со следующими параметрами: \"ИМЯ_ФАЙЛА_ДЛЯ_ЧТЕНИЯ\" \"ИМЯ_ФАЙЛА_ДЛЯ_ВЫВОДА\"");
            System.exit(0);
        }
    }
}

package ru.maxvagan.services.impls;

import org.springframework.stereotype.Service;
import ru.maxvagan.exceptions.EmployeeNotFoundException;
import ru.maxvagan.mainclasses.Employee;
import ru.maxvagan.mainclasses.GeneratorEmployee;
import ru.maxvagan.services.DepartmentService;
import ru.maxvagan.services.EmployeeBookService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final GeneratorEmployee humanResourceManager = new GeneratorEmployee(30_000.00f, 150_000.00f);
    private EmployeeBookService employeeBookService;
    private final String MAIN_URL = "<a href='http://localhost:8080/departments'>В Меню</a>";

    public DepartmentServiceImpl(EmployeeBookService employeeBookService) {
        this.employeeBookService = employeeBookService;
    }

    @Override
    public String getHelpInfo() {
        return "<h1>Справочная информация по работе с книгой сотрудников!</h1>" +
                "<td><tr><h3><a href='http://localhost:8080/departments/fillBook?count_staff=10&subdep=1'><b>Для заполнения книги штатом 10-ью сотрудниками</b></a></h3></tr>" +
                "<tr><h3>    Или выполните команду - <b>/fillBook?count_staff=..., где ... - значение кол-во сотрудников в штате</b></h3></tr>" +
                "<tr><h3><a href='http://localhost:8080/departments/allindepartment?departmentId=1'><b>Для вывода штата сотрудников</b></a></h3></tr>" +
                "<tr><h3>    Или выполните команду - <b>/allindepartment?departmentId=..., где ... - ID департамента</b></h3></tr>" +
                "<tr><h3><a href='http://localhost:8080/departments/add?firstName=ивана&lastName=Ивановича'><b>Для добавления в штат \"ивана Ивановича\"</b></a></h3></tr>" +
                "<tr><h3>    Или выполните команду - <b>/add?firstName=...&lastName=...</b>, где ... - значение имени или отчества</h3></tr>" +
                "<tr><h3><a href='http://localhost:8080/departments/remove?firstName=Сид0р4&lastName=Петровича'><b>Для удаления из штата \"Сид0р4 Петровича\"</b></a></h3></tr>" +
                "<tr><h3>    Или выполните команду - <b>/remove?firstName=...&lastName=...</b>, где ... - значение имени или отчества</h3></tr>" +
                "<tr><h3><a href='http://localhost:8080/departments/find?firstName=&lastName=Johnson'><b>Для поиска сотрудника \" Johnson\"</b></a></h3></tr>" +
                "<tr><h3>    Или выполните команду - <b>/find?firstName=...&lastName=...</b>, где ... - значение имени или отчества</h3></tr></td>";
    }

    @Override
    public String fillSubDepartment(int inpStaffCount, short inpSubDepID) {
        String workersName = "";
        String workersLastName = "";
        float workerSalary = 0.00f;
        short workerSubDepartment = 0;

        employeeBookService.cleanBook();
        for (int i = 0; i < inpStaffCount; i++) {
            workersName = humanResourceManager.giveAnEmployeeName();
            workersLastName = humanResourceManager.giveAnEmployeeLastName();
            workerSalary = humanResourceManager.assignSalary();
            workerSubDepartment = humanResourceManager.assignDepartment();
            employeeBookService.addEmployeeToBook(workersName, workersLastName, workerSubDepartment, workerSalary);
        }
        return String.format("Книга заполнена!;Всего в штате: %s;%s", employeeBookService.getBookSize(), MAIN_URL);
    }



    @Override
    public String getMaxSalaryEmployeeInSubDep(short inpSubDep) {
        Optional<Employee> winnerWorker = employeeBookService.getBookOfStaff().stream()
                .filter(e -> e.getSubDepartment()==inpSubDep)
                .max(Comparator.comparing(e -> e.getSalary()));
        return winnerWorker.orElseThrow(() -> new EmployeeNotFoundException("Нет сотрудников в отделе с максимальной ЗП")).toString();
    }
    @Override
    public String getMinSalaryEmployeeInSubDep(short inpSubDep) {
        Optional<Employee> winnerWorker = employeeBookService.getBookOfStaff().stream()
                .filter(e -> e.getSubDepartment()==inpSubDep)
                .min(Comparator.comparing(e -> e.getSalary()));
        return winnerWorker.orElseThrow(() -> new EmployeeNotFoundException("Нет сотрудников в отделе с минимальной ЗП")).toString();
    }

    @Override
    public String showListOfStaffOfSubDep(short inpSubDep) {
        List<String> EmployeeList = employeeBookService.getBookOfStaff().stream()
                .filter(e -> e.getSubDepartment() == inpSubDep)
                .map(e -> "<tr><h3>" + e.toString() + "</h3></tr>")
                .collect(Collectors.toList());
        return String.join("", EmployeeList);
    }

}

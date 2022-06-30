package ru.maxvagan.services.impls;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.maxvagan.exceptions.BadRequestException;
import ru.maxvagan.exceptions.EmployeeAlreadyAddedException;
import ru.maxvagan.exceptions.EmployeeNotFoundException;
import ru.maxvagan.exceptions.EmployeeStorageIsFullException;
import ru.maxvagan.mainclasses.Employee;
import ru.maxvagan.mainclasses.GeneratorEmployee;
import ru.maxvagan.services.EmployeeBookService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeBookServiceImpl implements EmployeeBookService {
    private final GeneratorEmployee humanResourceManager = new GeneratorEmployee(30_000.00f, 150_000.00f);
    private List<Employee> bookOfStaff = new ArrayList<>();
    private int maximumStaffCount = 0;
    private final String MAIN_URL = "<a href='http://localhost:8080/departments'>В Меню</a>";

    private int getEmployee(String inpName, String inpLastName) {
        int workerOptindex = bookOfStaff.stream()
                .map(e -> e.hashCode())
                .collect(Collectors.toList())
                .indexOf(Objects.hash(inpName, inpLastName));
        if (workerOptindex == -1) throw new EmployeeNotFoundException("Сотрудник не найден");
        return workerOptindex;
    }

    private boolean checkIfStrSatisfy(String inpStr) {
        if (StringUtils.isEmpty(inpStr) || StringUtils.isBlank(inpStr)) return false;
        boolean isFirstUppercase = StringUtils.equals(StringUtils.left(inpStr, 1), StringUtils.left(inpStr, 1).toUpperCase());
        if (!StringUtils.isAlphaSpace(inpStr) || !isFirstUppercase)
            return false;
        else
            return true;
    }

    @Override
    public String getHelpInfo() {
        return "<h1>Справочная информация по работе с книгой сотрудников!</h1>" +
                "<td><tr><h3><a href='http://localhost:8080/departments/fillBook?count_staff=10'><b>Для заполнения книги штатом 10-ью сотрудниками</b></a></h3></tr>" +
                "<tr><h3>    Или выполните команду - <b>/fillBook?count_staff=..., где ... - значение кол-во сотрудников в штате</b></h3></tr>" +
                "<tr><h3><a href='http://localhost:8080/departments/all'><b>Для вывода штата сотрудников</b></a></h3></tr>" +
                "<tr><h3>    Или выполните команду - <b>/all</b></h3></tr>" +
                "<tr><h3><a href='http://localhost:8080/departments/add?firstName=ивана&lastName=Ивановича'><b>Для добавления в штат \"ивана Ивановича\"</b></a></h3></tr>" +
                "<tr><h3>    Или выполните команду - <b>/add?firstName=...&lastName=...</b>, где ... - значение имени или отчества</h3></tr>" +
                "<tr><h3><a href='http://localhost:8080/departments/remove?firstName=Сид0р4&lastName=Петровича'><b>Для удаления из штата \"Сид0р4 Петровича\"</b></a></h3></tr>" +
                "<tr><h3>    Или выполните команду - <b>/remove?firstName=...&lastName=...</b>, где ... - значение имени или отчества</h3></tr>" +
                "<tr><h3><a href='http://localhost:8080/departments/find?firstName=&lastName=Johnson'><b>Для поиска сотрудника \" Johnson\"</b></a></h3></tr>" +
                "<tr><h3>    Или выполните команду - <b>/find?firstName=...&lastName=...</b>, где ... - значение имени или отчества</h3></tr></td>";
    }

    @Override
    public String fillStaffBook(int inpStaffCount) {
        maximumStaffCount = inpStaffCount;
        String workersName = "";
        String workersLastName = "";
        float workerSalary = 0.00f;
        short workerSubDepartment = 0;
        if (!bookOfStaff.isEmpty()) bookOfStaff.clear();
        for (int i = 0; i < inpStaffCount; i++) {
            workersName = humanResourceManager.giveAnEmployeeName();
            workersLastName = humanResourceManager.giveAnEmployeeLastName();
            workerSalary = humanResourceManager.assignSalary();
            workerSubDepartment = humanResourceManager.assignDepartment();
//            addEmployeeToBook(workersName, workersLastName);
            bookOfStaff.add(new Employee(workersName, workersLastName, workerSubDepartment, workerSalary));
        }
        return String.format("Книга заполнена!;Всего в штате: %s;%s", bookOfStaff.size(), MAIN_URL);
    }

    @Override
    public String addEmployeeToBook(String inpName, String inpLastName) {
        if (!checkIfStrSatisfy(inpName) || !checkIfStrSatisfy(inpLastName)) throw new BadRequestException("400 Bad Request Params");
        boolean doesEmployeeInState = bookOfStaff.stream()
                .filter(e -> e.getName().equals(inpName) &&
                        e.getLastname().equals(inpLastName))
                .findFirst()
                .isEmpty();
        if (bookOfStaff.size() == maximumStaffCount && bookOfStaff.size() > 0)
            throw new EmployeeStorageIsFullException("Штат сотрудников переполнен!");
        Employee worker = new Employee(inpName, inpLastName, humanResourceManager.assignDepartment(), humanResourceManager.assignSalary());
        if (doesEmployeeInState)
            throw new EmployeeAlreadyAddedException(String.format("Сотрудник %s уже добавлен в штат!", worker));
        bookOfStaff.add(worker);
        return String.format("Добавлен новый сотрудник в штат: %s;Всего в штате: %s;%s",
                worker,
                bookOfStaff.size(),
                MAIN_URL);
    }

    @Override
    public String deleteEmployeeFromBook(String inpName, String inpLastName) {
        if (!checkIfStrSatisfy(inpName) || !checkIfStrSatisfy(inpLastName)) throw new BadRequestException("400 Bad Request Params");
        int workerIdx = -1;
        String messageStr = "";
        Employee worker = new Employee(inpName, inpLastName, (short) 1, 0.00f);
        try {
            workerIdx = getEmployee(inpName, inpLastName);
            bookOfStaff.remove(workerIdx);
            messageStr = String.format("Сотрудник %s был удален", worker);
        } catch (EmployeeNotFoundException e) {
            messageStr = String.format("Сотрудник %s не найден!", worker);
            e.printStackTrace();
        } finally {
            messageStr = String.format("%s;Всего в штате: %s;%s", messageStr, bookOfStaff.size(), MAIN_URL);
        }
        return messageStr;
    }

    @Override
    public String getMaxSalaryEmployeeInSubDep(short inpSubDep) {
        Optional<Employee> winnerWorker = bookOfStaff.stream()
                .filter(e -> e.getSubDepartment()==inpSubDep)
                .max(Comparator.comparing(e -> e.getSalary()));
        return winnerWorker.orElseThrow(() -> new EmployeeNotFoundException("Нет сотрудников в отделе с максимальной ЗП")).toString();
    }

    @Override
    public String getMinSalaryEmployeeInSubDep(short inpSubDep) {
        Optional<Employee> winnerWorker = bookOfStaff.stream()
                .filter(e -> e.getSubDepartment()==inpSubDep)
                .min(Comparator.comparing(e -> e.getSalary()));
        return winnerWorker.orElseThrow(() -> new EmployeeNotFoundException("Нет сотрудников в отделе с минимальной ЗП")).toString();
    }

    @Override
    public String showListOfStaffOfSubDep(short inpSubDep) {
        List<String> EmployeeList = bookOfStaff.stream()
                .filter(e -> e.getSubDepartment() == inpSubDep)
                .map(e -> "<tr><h3>" + e.toString() + "</h3></tr>")
                .collect(Collectors.toList());
        return String.join("", EmployeeList);
    }

    @Override
    public String findEmployee(String inpName, String inpLastName) {
        if (!checkIfStrSatisfy(inpName) || !checkIfStrSatisfy(inpLastName)) throw new BadRequestException("400 Bad Request Params");
        int workerIdx = -1;
        String messageStr = "";
        Employee worker = new Employee(inpName, inpLastName, (short) 0, 0.00f);
        try {
            workerIdx = getEmployee(inpName, inpLastName);
            worker = bookOfStaff.get(workerIdx);
            messageStr = String.format("Найден сотрудник %s", worker);
        } catch (EmployeeNotFoundException e) {
            messageStr = String.format("Сотрудник %s не найден!", worker);
            e.printStackTrace();
        } finally {
            messageStr = String.format("%s;Всего в штате: %s;%s", messageStr, bookOfStaff.size(), MAIN_URL);
        }
        return messageStr;
    }

    @Override
    public String showListOfStaff() {
        List<String> EmployeeList = bookOfStaff.stream()
                .sorted(Comparator.comparing(e -> e.getSubDepartment()))
                .map(e -> "<tr><h3>" + e.toString() + "</h3></tr>")
                .collect(Collectors.toList());
        return String.join("", EmployeeList);
    }
}

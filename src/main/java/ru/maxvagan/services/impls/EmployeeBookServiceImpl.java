package ru.maxvagan.services.impls;

import org.springframework.stereotype.Service;
import ru.maxvagan.exceptions.EmployeeAlreadyAddedException;
import ru.maxvagan.exceptions.EmployeeNotFoundException;
import ru.maxvagan.exceptions.EmployeeStorageIsFullException;
import ru.maxvagan.mainclasses.Employee;
import ru.maxvagan.mainclasses.GeneratorEmployee;
import ru.maxvagan.services.EmployeeBookService;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeBookServiceImpl implements EmployeeBookService {
    private final GeneratorEmployee humanResourceManager = new GeneratorEmployee();
    private Map<String, Employee> bookOfStaff = new HashMap<>();
    private int maximumStaffCount = 0;

    private String getEmployee(String inpName, String inpLastName) {
        Employee worker = bookOfStaff.get(inpName + inpLastName);
        if (worker == null) throw new EmployeeNotFoundException("Сотрудник не найден");
        return inpName + inpLastName;
    }

    @Override
    public String getHelpInfo() {
        return "<h1>Справочная информация по работе с книгой сотрудников!</h1>" +
                "<td><tr><h3>Для заполнения книги штатом сотрудников выполните команду - <b>/fillBook?count_staff=..., где ... - значение кол-во сотрудников в штате</b></h3></tr>" +
                "<tr><h3>Для вывода штата сотрудников выполните команду - <b>/showBook</b></h3></tr>" +
                "<tr><h3>Для добавления сотрудника в штат выполните команду - <b>/add?firstName=...&lastName=...</b>, где ... - значение имени или отчества</h3></tr>" +
                "<tr><h3>Для удаления сотрудника из штата выполните команду - <b>/remove?firstName=...&lastName=...</b>, где ... - значение имени или отчества</h3></tr>" +
                "<tr><h3>Для поиска сотрудника выполните команду - <b>/find?firstName=...&lastName=...</b>, где ... - значение имени или отчества</h3></tr></td>";
    }

    @Override
    public String fillStaffBook(int inpStaffCount) {
        maximumStaffCount = inpStaffCount;
        String workersName = "";
        String workersLastName = "";
        if (!bookOfStaff.isEmpty()) bookOfStaff.clear();
        for (int i = 0; i < inpStaffCount; i++) {
            workersName = humanResourceManager.giveAnEmployeeName();
            workersLastName = humanResourceManager.giveAnEmployeeLastName();
            if (!bookOfStaff.containsKey(workersName + workersLastName))
                bookOfStaff.put(workersName + workersLastName, new Employee(workersName, workersLastName));
        }
        return String.format("Книга заполнена!;Всего в штате: %s", bookOfStaff.size());
    }

    @Override
    public String addEmployeeToBook(String inpName, String inpLastName) {
        boolean doesEmployeeInState = bookOfStaff.containsKey(inpName + inpLastName);
        if (bookOfStaff.size() == maximumStaffCount && bookOfStaff.size() > 0)
            throw new EmployeeStorageIsFullException("Штат сотрудников переполнен!");
        Employee worker = new Employee(inpName, inpLastName);
        if (doesEmployeeInState)
            throw new EmployeeAlreadyAddedException(String.format("Сотрудник %s уже добавлен в штат!", worker));
        bookOfStaff.put(inpName + inpLastName, worker);
        return String.format("Добавлен новый сотрудник в штат: %s;Всего в штате: %s",
                worker,
                bookOfStaff.size());
    }

    @Override
    public String deleteEmployeeFromBook(String inpName, String inpLastName) {
        String mapKey = "";
        String messageStr = "";
        Employee worker = new Employee(inpName, inpLastName);
        try {
            mapKey = getEmployee(inpName, inpLastName);
            bookOfStaff.remove(mapKey);
            messageStr = String.format("Сотрудник %s был удален", worker);
        } catch (EmployeeNotFoundException e) {
            messageStr = String.format("Сотрудник %s не найден!", worker);
            e.printStackTrace();
        } finally {
            messageStr = String.format("%s;Всего в штате: %s", messageStr, bookOfStaff.size());
        }
        return messageStr;
    }

    @Override
    public String findEmployee(String inpName, String inpLastName) {
        String maxKey = "";
        String messageStr = "";
        Employee worker = new Employee(inpName, inpLastName);
        try {
            maxKey = getEmployee(inpName, inpLastName);
            messageStr = String.format("Найден сотрудник %s", bookOfStaff.get(maxKey));
        } catch (EmployeeNotFoundException e) {
            messageStr = String.format("Сотрудник %s не найден!", worker);
            e.printStackTrace();
        } finally {
            messageStr = String.format("%s;Всего в штате: %s", messageStr, bookOfStaff.size());
        }
        return messageStr;
    }

    @Override
    public Map<String, Employee> showListOfStaff() {
        return bookOfStaff;
    }
}

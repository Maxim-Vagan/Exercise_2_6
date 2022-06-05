package ru.maxvagan.services.impls;

import org.springframework.stereotype.Service;
import ru.maxvagan.exceptions.EmployeeAlreadyAddedException;
import ru.maxvagan.exceptions.EmployeeNotFoundException;
import ru.maxvagan.exceptions.EmployeeStorageIsFullException;
import ru.maxvagan.mainclasses.Employee;
import ru.maxvagan.mainclasses.GeneratorEmployee;
import ru.maxvagan.services.EmployeeBookService;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeBookServiceImpl implements EmployeeBookService {
    private GeneratorEmployee humanResourceManager = new GeneratorEmployee();
    private List<Employee> bookOfStaff = new ArrayList<Employee>();
    private int maximumStaffCount = 0;

    private int getEmployeeIndex(String inpName, String inpLastName) {
        int foundIndex = -1;
        if (bookOfStaff.isEmpty()) throw new EmployeeNotFoundException("Сотрудник не найден");
        for (int i = 0; i < bookOfStaff.size(); i++) {
            if (bookOfStaff.get(i).getName().equals(inpName) && bookOfStaff.get(i).getLastname().equals(inpLastName)) {
                foundIndex = i;
                break;
            }
        }
        if (foundIndex < 0) throw new EmployeeNotFoundException("Сотрудник не найден");
        return foundIndex;
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
        for (int i = 0; i < inpStaffCount; i++) {
            bookOfStaff.add(new Employee(humanResourceManager.giveAnEmployeeName(),
                    humanResourceManager.giveAnEmployeeLastName()));
        }
        return String.format("Книга заполнена!;Всего в штате: %s", bookOfStaff.size());
    }

    @Override
    public String addEmployeeToBook(String inpName, String inpLastName) {
        int employeeIndex = -1;
        String messageStr = "";
        Employee worker = new Employee(inpName, inpLastName);
        if (bookOfStaff.size() == maximumStaffCount && bookOfStaff.size() > 0)
            throw new EmployeeStorageIsFullException("Штат сотрудников переполнен!");
        try {
            employeeIndex = getEmployeeIndex(inpName, inpLastName);
        } catch (EmployeeNotFoundException e) {
            messageStr = "Поступил Новый сотрудник";
        } finally {
            if (employeeIndex >= 0)
                throw new EmployeeAlreadyAddedException(String.format("Сотрудник %s уже добавлен в штат!", worker));
            bookOfStaff.add(worker);
            return String.format("%s;Добавлен новый сотрудник в штат: %s;Всего в штате: %s",
                    messageStr,
                    worker,
                    bookOfStaff.size());
        }
    }

    @Override
    public String deleteEmployeeFromBook(String inpName, String inpLastName) {
        int employeeIndex = -1;
        String messageStr = "";
        Employee worker = new Employee(inpName, inpLastName);
        try {
            employeeIndex = getEmployeeIndex(inpName, inpLastName);
            bookOfStaff.remove(employeeIndex);
            messageStr = String.format("Сотрудник %s был удален", worker);
        } catch (EmployeeNotFoundException e) {
            messageStr = String.format("Сотрудник %s не найден!", worker);
            e.printStackTrace();
        } finally {
            return String.format("%s;Всего в штате: %s", messageStr, bookOfStaff.size());
        }
    }

    @Override
    public String findEmployee(String inpName, String inpLastName) {
        int employeeIndex = -1;
        String messageStr = "";
        Employee worker = new Employee(inpName, inpLastName);
        try {
            employeeIndex = getEmployeeIndex(inpName, inpLastName);
            messageStr = String.format("Найден сотрудник %s", bookOfStaff.get(employeeIndex));
        } catch (EmployeeNotFoundException e) {
            messageStr = String.format("Сотрудник %s не найден!", worker);
            e.printStackTrace();
        } finally {
            return String.format("%s;Всего в штате: %s", messageStr, bookOfStaff.size());
        }
    }

    @Override
    public List<Employee> showListOfStaff() {
        return bookOfStaff;
    }
}

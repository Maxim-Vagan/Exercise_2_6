package ru.maxvagan.services.impls;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.maxvagan.exceptions.BadRequestException;
import ru.maxvagan.exceptions.EmployeeAlreadyAddedException;
import ru.maxvagan.exceptions.EmployeeNotFoundException;
import ru.maxvagan.exceptions.EmployeeStorageIsFullException;
import ru.maxvagan.mainclasses.Employee;
import ru.maxvagan.services.EmployeeBookService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmployeeBookServiceImpl implements EmployeeBookService {
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
    public String addEmployeeToBook( String inpName, String inpLastName, short inpSubDepartment, float salary) {
        if (!checkIfStrSatisfy(inpName) || !checkIfStrSatisfy(inpLastName)) throw new BadRequestException("400 Bad Request Params");
        boolean employeeNotInState = bookOfStaff.stream()
                .filter(e -> e.getName().equals(inpName) &&
                        e.getLastname().equals(inpLastName))
                .findFirst()
                .isEmpty();
        if (bookOfStaff.size() == maximumStaffCount && bookOfStaff.size() > 0)
            throw new EmployeeStorageIsFullException("Штат сотрудников переполнен!");
        Employee worker = new Employee(inpName, inpLastName, inpSubDepartment, salary);
        if (!employeeNotInState)
            throw new EmployeeAlreadyAddedException(String.format("Сотрудник %s уже добавлен в штат!", worker));
        bookOfStaff.add(worker);
        return String.format("Добавлен новый сотрудник в штат: %s;Всего в штате: %s;%s",
                worker,
                bookOfStaff.size(),
                MAIN_URL);
    }

    @Override
    public List<Employee> getBookOfStaff() {
        return bookOfStaff;
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
    public boolean cleanBook() {
        if (!bookOfStaff.isEmpty()) {
            bookOfStaff.clear();
            return true;
        } else {return false;}
    }

    @Override
    public int getBookSize() {
        return bookOfStaff.size();
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

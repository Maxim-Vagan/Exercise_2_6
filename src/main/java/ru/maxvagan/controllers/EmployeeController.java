package ru.maxvagan.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.maxvagan.exceptions.EmployeeAlreadyAddedException;
import ru.maxvagan.exceptions.EmployeeNotFoundException;
import ru.maxvagan.exceptions.EmployeeStorageIsFullException;
import ru.maxvagan.mainclasses.Employee;
import ru.maxvagan.services.EmployeeBookService;

import java.util.Map;

@RestController
@RequestMapping(path = "/employee")
public class EmployeeController {
    private final EmployeeBookService employeeBookService;

    public EmployeeController(EmployeeBookService employeeBookService) {
        this.employeeBookService = employeeBookService;
    }

    @GetMapping
    public String index() {
        return employeeBookService.getHelpInfo();
    }

    @GetMapping(path = "/error")
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR , reason = "Проверьте, пожалуйста, параметры запроса. Убедитесь, что операция целесообразна")
    public String showError() {
        return "Приносим извинения! Возник сбой при выполнении функций";
    }

    @GetMapping(path = "/fillBook")
    public String fillStaffBook(@RequestParam("count_staff") int inpQuantity) {
        String output = employeeBookService.fillStaffBook(inpQuantity);
        return "<td><tr><h3>" + output.replace(";", "</h3></tr><tr><h3>") + "</h3></tr></td>";
    }

    @GetMapping(path = "/add")
    public String addEmployeeToBook(@RequestParam("firstName") String inpName, @RequestParam("lastName") String inpLastName) {
        String respStr = "";
        try {
            String output = employeeBookService.addEmployeeToBook(inpName, inpLastName);
            respStr = "<tr><h3>" + output.replace(";", "</h3></tr><tr><h3>") + "</h3></tr>";
        } catch (EmployeeStorageIsFullException e) {
            respStr = "<tr><h3>Штат уже полон дождитесь появления вакансии!</h3></tr>";
            e.printStackTrace();
        } catch (EmployeeAlreadyAddedException e) {
            respStr = String.format("<tr><h3>Сотрудник %s %s уже состоит в штате</h3></tr>", inpName, inpLastName);
            e.printStackTrace();
        } finally {
            respStr = "<td>"+respStr+"</td>";
        }
        return respStr;
    }

    @GetMapping(path = "/remove")
    public String deleteEmployeeFromBook(@RequestParam("firstName") String inpName, @RequestParam("lastName") String inpLastName) {
        String respStr = "";
        try {
            String output = employeeBookService.deleteEmployeeFromBook(inpName, inpLastName);
            respStr = "<tr><h3>" + output.replace(";", "</h3></tr><tr><h3>") + "</h3></tr>";
        } catch (EmployeeNotFoundException e) {
            respStr = String.format("<tr><h3>Увольняемый сотрудник %s %s не найден!</h3></tr>", inpName, inpLastName);
            e.printStackTrace();
        } finally {
            respStr = "<td>"+respStr+"</td>";
        }
        return respStr;
    }

    @GetMapping(path = "/find")
    public String findEmployee(@RequestParam("firstName") String inpName, @RequestParam("lastName") String inpLastName) {
        String respStr = "";
        try {
            String output = employeeBookService.findEmployee(inpName, inpLastName);
            respStr = "<tr><h3>" + output.replace(";", "</h3></tr><tr><h3>") + "</h3></tr>";
        } catch (EmployeeNotFoundException e) {
            respStr = String.format("<tr><h3>Сотрудник %s %s не найден!</h3></tr>", inpName, inpLastName);
            e.printStackTrace();
        } finally {
            respStr = "<td>"+respStr+"</td>";
        }
        return respStr;
    }

    @GetMapping(path = "/showBook")
    public String showListOfStaff() {
        Map<String, Employee> output = employeeBookService.showListOfStaff();
        StringBuilder listOfStaff = new StringBuilder();
        listOfStaff.append("<h2>Список штатных сотрудников</h2><td>");
        for (Employee employee : output.values()) {
            listOfStaff.append("<tr><h3>").append(employee.toString()).append("</h3></tr>");
        }
        listOfStaff.append("</td>");
        return listOfStaff.toString();
    }
}

package ru.maxvagan.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.maxvagan.mainclasses.Employee;
import ru.maxvagan.services.EmployeeBookService;

import java.util.List;

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

    @GetMapping(path = "/fillBook")
    public String fillStaffBook(@RequestParam("count_staff") int inpQuantity) {
        String output = employeeBookService.fillStaffBook(inpQuantity);
        return "<td><tr><h3>" + output.replace(";", "</h3></tr><tr><h3>") + "</h3></tr></td>";
    }

    @GetMapping(path = "/add")
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Проверьте, пожалуйста, корректность параметров запроса")
    public String addEmployeeToBook(@RequestParam("firstName") String inpName, @RequestParam("lastName") String inpLastName) {
        String output = employeeBookService.addEmployeeToBook(inpName, inpLastName);
        return "<td><tr><h3>" + output.replace(";", "</h3></tr><tr><h3>") + "</h3></tr></td>";
    }

    @GetMapping(path = "/remove")
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Проверьте, пожалуйста, корректность параметров запроса")
    public String deleteEmployeeFromBook(@RequestParam("firstName") String inpName, @RequestParam("lastName") String inpLastName) {
        String output = employeeBookService.deleteEmployeeFromBook(inpName, inpLastName);
        return "<td><tr><h3>" + output.replace(";", "</h3></tr><tr><h3>") + "</h3></tr></td>";
    }

    @GetMapping(path = "/find")
    public String findEmployee(@RequestParam("firstName") String inpName, @RequestParam("lastName") String inpLastName) {
        String output = employeeBookService.findEmployee(inpName, inpLastName);
        return "<td><tr><h3>" + output.replace(";", "</h3></tr><tr><h3>") + "</h3></tr></td>";
    }

    @GetMapping(path = "/showBook")
    public String showListOfStaff() {
        List<Employee> output = employeeBookService.showListOfStaff();
        StringBuilder listOfStaff = new StringBuilder();
        listOfStaff.append("<h2>Список штатных сотрудников</h2><td>");
        for (Employee employee : output) {
            listOfStaff.append("<tr><h3>" + employee.toString() + "</h3></tr>");
        }
        listOfStaff.append("</td>");
        return listOfStaff.toString();
    }
}

package ru.maxvagan.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.maxvagan.services.EmployeeBookService;

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
    public String addEmployeeToBook(@RequestParam("firstName") String inpName, @RequestParam("lastName") String inpLastName) {
        String output = employeeBookService.addEmployeeToBook(inpName, inpLastName);
        return "<td><tr><h3>" + output.replace(";", "</h3></tr><tr><h3>") + "</h3></tr></td>";
    }

    @GetMapping(path = "/remove")
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
        String output = employeeBookService.showListOfStaff();
        return "<td><tr><h3>" + output.replace(";", "</h3></tr><tr><h3>") + "</h3></tr></td>";
    }
}

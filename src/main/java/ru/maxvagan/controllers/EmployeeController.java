package ru.maxvagan.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.maxvagan.exceptions.BadRequestException;
import ru.maxvagan.exceptions.EmployeeAlreadyAddedException;
import ru.maxvagan.exceptions.EmployeeNotFoundException;
import ru.maxvagan.exceptions.EmployeeStorageIsFullException;
import ru.maxvagan.services.DepartmentService;
import ru.maxvagan.services.EmployeeBookService;

@RestController
@RequestMapping(path = "/departments")
public class EmployeeController {

    private final DepartmentService departmentService;
    private final EmployeeBookService employeeBookService;

    private final String MAIN_URL = "<a href='http://localhost:8080/departments'>В Меню</a>";

    public EmployeeController(DepartmentService departmentService, EmployeeBookService employeeBookService) {
        this.departmentService = departmentService;
        this.employeeBookService = employeeBookService;
    }

    @GetMapping
    public String index() {
        return departmentService.getHelpInfo();
    }

    @GetMapping(path = "/error")
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Проверьте, пожалуйста, параметры запроса. Убедитесь, что операция целесообразна")
    public String showError() {
        return "Приносим извинения! Возник сбой при выполнении функций";
    }

    @GetMapping(path = "/fillBook")
    public String fillStaffBook(@RequestParam("count_staff") int inpQuantity, @RequestParam("subdep") short inpSubDepId) {
        String output = departmentService.fillSubDepartment(inpQuantity, inpSubDepId);
        return "<td><tr><h3>" + output.replace(";", "</h3></tr><tr><h3>") + "</h3></tr></td>";
    }

    @GetMapping(path = "/add")
    public String addEmployeeToBook(@RequestParam("firstName") String inpName, @RequestParam("lastName") String inpLastName,
                                    @RequestParam("subDepID") short inpsubDep, @RequestParam("salary") float inpSalary) {
        String respStr = "";
        try {
            String output = employeeBookService.addEmployeeToBook(inpName, inpLastName, inpsubDep, inpSalary);
            respStr = "<tr><h3>" + output.replace(";", "</h3></tr><tr><h3>") + "</h3></tr>";
        } catch (BadRequestException e) {
            respStr = String.format("<tr><h3>Значение \"%s\" и/или \"%s\" не корректно</h3></tr>", inpName, inpLastName);
            e.printStackTrace();
        } catch (EmployeeStorageIsFullException e) {
            respStr = "<tr><h3>Штат уже полон дождитесь появления вакансии!</h3></tr>";
            e.printStackTrace();
        } catch (EmployeeAlreadyAddedException e) {
            respStr = String.format("<tr><h3>Сотрудник %s %s уже состоит в штате</h3></tr>", inpName, inpLastName);
            e.printStackTrace();
        } finally {
            respStr = "<td>" + respStr + "</td>";
        }
        return respStr;
    }

    @GetMapping(path = "/remove")
    public String deleteEmployeeFromBook(@RequestParam("firstName") String inpName, @RequestParam("lastName") String inpLastName) {
        String respStr = "";
        try {
            String output = employeeBookService.deleteEmployeeFromBook(inpName, inpLastName);
            respStr = "<tr><h3>" + output.replace(";", "</h3></tr><tr><h3>") + "</h3></tr>";
        } catch (BadRequestException e) {
            respStr = String.format("<tr><h3>Значение \"%s\" и/или \"%s\" не корректно</h3></tr>", inpName, inpLastName);
            e.printStackTrace();
        } catch (EmployeeNotFoundException e) {
            respStr = String.format("<tr><h3>Увольняемый сотрудник %s %s не найден!</h3></tr>", inpName, inpLastName);
            e.printStackTrace();
        } finally {
            respStr = "<td>" + respStr + "</td>";
        }
        return respStr;
    }

    @GetMapping(path = "/find")
    public String findEmployee(@RequestParam("firstName") String inpName, @RequestParam("lastName") String inpLastName) {
        String respStr = "";
        try {
            String output = employeeBookService.findEmployee(inpName, inpLastName);
            respStr = "<tr><h3>" + output.replace(";", "</h3></tr><tr><h3>") + "</h3></tr>";
        } catch (BadRequestException e) {
            respStr = String.format("<tr><h3>Значение \"%s\" и/или \"%s\" не корректно</h3></tr>", inpName, inpLastName);
            e.printStackTrace();
        } catch (EmployeeNotFoundException e) {
            respStr = String.format("<tr><h3>Сотрудник %s %s не найден!</h3></tr>", inpName, inpLastName);
            e.printStackTrace();
        } finally {
            respStr = "<td>" + respStr + "</td>";
        }
        return respStr;
    }

    @GetMapping(path = "/max-salary")
    public String getMaxSalaryEmployeeInSubDep(@RequestParam("departmentId") Short inpDepIdx) {
        String output = departmentService.getMaxSalaryEmployeeInSubDep(inpDepIdx);
        StringBuilder listOfStaff = new StringBuilder();
        listOfStaff.append("<h2>Сотрудник в департаменте " + inpDepIdx + " с макс. ЗП</h2><td>");
        listOfStaff.append("<tr><h3>").append(output).append("</h3></tr>");
        listOfStaff.append("<tr><h3>").append(MAIN_URL).append("</h3></tr>");
        listOfStaff.append("</td>");
        return listOfStaff.toString();
    }

    @GetMapping(path = "/min-salary")
    public String getMinSalaryEmployeeInSubDep(@RequestParam("departmentId") Short inpDepIdx) {
        String output = departmentService.getMinSalaryEmployeeInSubDep(inpDepIdx);
        StringBuilder listOfStaff = new StringBuilder();
        listOfStaff.append("<h2>Сотрудник в департаменте " + inpDepIdx + " с мин. ЗП</h2><td>");
        listOfStaff.append("<tr><h3>").append(output).append("</h3></tr>");
        listOfStaff.append("<tr><h3>").append(MAIN_URL).append("</h3></tr>");
        listOfStaff.append("</td>");
        return listOfStaff.toString();
    }

    @GetMapping(path = "/allindepartment")
    public String showListOfStaffOfSubDep(@RequestParam("departmentId") Short inpDepIdx) {
        String output = departmentService.showListOfStaffOfSubDep(inpDepIdx);
        StringBuilder listOfStaff = new StringBuilder();
        listOfStaff.append("<h2>Список сотрудников в департаменте " + inpDepIdx + "</h2><td>");
        listOfStaff.append("").append(output).append("</h3></tr>");
        listOfStaff.append("<tr><h3>").append(MAIN_URL).append("</h3></tr>");
        listOfStaff.append("</td>");
        return listOfStaff.toString();
    }

    @GetMapping(path = "/all")
    public String showListOfStaff() {
        String output = employeeBookService.showListOfStaff();
        StringBuilder listOfStaff = new StringBuilder();
        listOfStaff.append("<h2>Список штатных сотрудников</h2><td>");
        listOfStaff.append("<tr><h3>").append(output).append("</h3></tr>");
        listOfStaff.append("<tr><h3>").append(MAIN_URL).append("</h3></tr>");
        listOfStaff.append("</td>");
        return listOfStaff.toString();
    }
}

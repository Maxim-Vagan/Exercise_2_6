package ru.maxvagan.services;

import ru.maxvagan.mainclasses.Employee;

import java.util.Map;

public interface EmployeeBookService {

    String getHelpInfo();
    String fillStaffBook(int inpStaffCount);
    String addEmployeeToBook(String inpName, String inpLastName);
    String deleteEmployeeFromBook(String inpName, String inpLastName);
    String findEmployee(String inpName, String inpLastName);
    Map<String, Employee> showListOfStaff();
}

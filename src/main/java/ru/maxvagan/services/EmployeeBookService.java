package ru.maxvagan.services;

import ru.maxvagan.mainclasses.Employee;

import java.util.List;

public interface EmployeeBookService {
    List<Employee> getBookOfStaff();

    boolean cleanBook();
    int getBookSize();
    String addEmployeeToBook(String inpName, String inpLastName, short inpSubDepartment, float salary);
    String deleteEmployeeFromBook(String inpName, String inpLastName);
    String findEmployee(String inpName, String inpLastName);
    String showListOfStaff();
}

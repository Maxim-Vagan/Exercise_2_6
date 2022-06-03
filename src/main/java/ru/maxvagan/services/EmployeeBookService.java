package ru.maxvagan.services;

public interface EmployeeBookService {

    String getHelpInfo();
    String fillStaffBook(int inpStaffCount);
    String addEmployeeToBook(String inpName, String inpLastName);
    String deleteEmployeeFromBook(String inpName, String inpLastName);
    String findEmployee(String inpName, String inpLastName);
    String showListOfStaff();
}

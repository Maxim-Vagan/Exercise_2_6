package ru.maxvagan.services;

public interface DepartmentService {

    String getHelpInfo();
    String fillSubDepartment(int inpStaffCount, short inpSubDepID);
    String getMaxSalaryEmployeeInSubDep(short inpSubDep);
    String getMinSalaryEmployeeInSubDep(short inpSubDep);
    String showListOfStaffOfSubDep(short inpSubDep);
}

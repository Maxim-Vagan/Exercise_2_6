package ru.maxvagan.mainclasses;

import java.util.Locale;
import java.util.Objects;

public class Employee {
    private final String name;
    private final String lastname;
    private short subDepartment;
    private float salary;

    public Employee(String name, String lastname, short subDepartment, float salary) {
        this.name = name;
        this.lastname = lastname;
        setSubDepartment(subDepartment);
        setSalary(salary);
    }

    private String financeSum(float inpSum, char thousandDelim, char decimalDelim){
        String[] result = String.format("%.2f", inpSum).replace('.', ',').split(",");
        return String.format(Locale.US, "%,d", Integer.valueOf(result[0])).replace(',', thousandDelim) + decimalDelim + result[1];
    }

    public String getName() {
        return name;
    }

    public String getLastname(){
        return lastname;
    }

    public short getSubDepartment() {
        return subDepartment;
    }

    public float getSalary() {
        return salary;
    }

    public void setSubDepartment(short subDepartment) {
        this.subDepartment = subDepartment;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return getName().equals(employee.getName()) && getLastname().equals(employee.getLastname());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getLastname());
    }

    @Override
    public String toString() {
        return "{ \"firstName\": \"" + name + "\"" +
                ", \"lastName\": \"" + lastname + "\""+
                ", \"subDepartment\": " + subDepartment +
                ", \"salary\": " + financeSum(salary, '_', '.') +
                " }";
    }
}

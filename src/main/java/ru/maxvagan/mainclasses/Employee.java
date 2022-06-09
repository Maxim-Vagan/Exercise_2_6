package ru.maxvagan.mainclasses;

import java.util.Objects;

public class Employee {
    private final String name;
    private final String lastname;

    public Employee(String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public String getLastname(){
        return lastname;
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
                ", \"lastName\": \"" + lastname + "\" }";
    }
}

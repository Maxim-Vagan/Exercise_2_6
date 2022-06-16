package ru.maxvagan.mainclasses;

public class GeneratorEmployee {
    private static String[] dictOfNames = {"Иван", "Петр", "Сидор", "Павел", "Максим", "Михаил", "Артем"};
    private static String[] dictOfSurNames = {"Иванов", "Петров", "Сидоров", "Павлов", "Максимов", "Михайлов", "Артемов"};
    private static String[] dictOfLastNames = {"Иванович", "Петрович", "Сидорович", "Павлович", "Максимович", "Михаилович", "Артемович"};
    private float startSalary;
    private float endSalary;

    public GeneratorEmployee(float startSalary, float endSalary) {
        this.startSalary = startSalary;
        this.endSalary = endSalary;
    }

    public String giveAnEmployeeName() {
        return dictOfNames[(int) Math.round(Math.random() * (dictOfNames.length-1))];
    }

    public String giveAnEmployeeSurName() {
        return dictOfSurNames[(int) Math.round(Math.random() * (dictOfSurNames.length-1))];
    }

    public String giveAnEmployeeLastName() {
        return dictOfLastNames[(int) Math.round(Math.random() * (dictOfLastNames.length-1))];
    }

    public short assignDepartment() {
        return (short)(1 + Math.round(Math.random() * 4));
    }

    public float assignSalary() {
        return (float)(startSalary + Math.random() * (endSalary-startSalary));
    }
}

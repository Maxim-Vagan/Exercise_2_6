package ru.maxvagan.mainclasses;

public class GeneratorEmployee {
    private static String[] dictOfNames = {"Иван", "Петр", "Сидор", "Павел", "Максим", "Михаил", "Артем"};
    private static String[] dictOfSurNames = {"Иванов", "Петров", "Сидоров", "Павлов", "Максимов", "Михайлов", "Артемов"};
    private static String[] dictOfLastNames = {"Иванович", "Петрович", "Сидорович", "Павлович", "Максимович", "Михаилович", "Артемович"};

    public String giveAnEmployeeName() {
        return dictOfNames[(int) Math.round(Math.random() * (dictOfNames.length-1))];
    }

    public String giveAnEmployeeSurName() {
        return dictOfSurNames[(int) Math.round(Math.random() * (dictOfSurNames.length-1))];
    }

    public String giveAnEmployeeLastName() {
        return dictOfLastNames[(int) Math.round(Math.random() * (dictOfLastNames.length-1))];
    }
}

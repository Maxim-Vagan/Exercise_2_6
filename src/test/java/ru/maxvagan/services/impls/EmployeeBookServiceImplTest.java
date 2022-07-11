package ru.maxvagan.services.impls;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.maxvagan.exceptions.BadRequestException;
import ru.maxvagan.exceptions.EmployeeAlreadyAddedException;
import ru.maxvagan.exceptions.EmployeeNotFoundException;
import ru.maxvagan.mainclasses.Employee;
import ru.maxvagan.services.EmployeeBookService;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmployeeBookServiceImplTest {
    private EmployeeBookService testEmployeeBookService;


    private static Stream<Arguments> getParamsForTests() {
        return Stream.of(Arguments.of("Иван", "Петрович", (short) 1, 10_123f),
                Arguments.of("Иван", "Иванович", (short) 2, 20_123f),
                Arguments.of(null, "Петрович", (short) 1, 30_123f),
                Arguments.of("Артем", "Пе2р0ви4", (short) 1, 40_123f)
        );
    }

    @BeforeEach
    void setUp() {
        testEmployeeBookService = new EmployeeBookServiceImpl();
        testEmployeeBookService.cleanBook();
        testEmployeeBookService.addEmployeeToBook("Иван", "Петрович", (short) 1, 10_000f);
        testEmployeeBookService.addEmployeeToBook("Петр", "Сидорович", (short) 1, 10_000f);
        testEmployeeBookService.addEmployeeToBook("Сидор", "Павлович", (short) 1, 10_000f);
        testEmployeeBookService.addEmployeeToBook("Павел", "Павлович", (short) 2, 10_000f);
        testEmployeeBookService.addEmployeeToBook("Максим", "Максимович", (short) 2, 10_000f);
    }

    @ParameterizedTest
    @MethodSource("getParamsForTests")
    void addEmployeeToBookTestBadRequestException(String inpName, String inpLastName, short inpSubDepartment, float salary) {
        assertThrows(BadRequestException.class,
                () -> testEmployeeBookService.addEmployeeToBook(inpName, inpLastName, inpSubDepartment, salary));
    }

    @ParameterizedTest
    @MethodSource("getParamsForTests")
    void addEmployeeToBookTestEmployeeExistException(String inpName, String inpLastName, short inpSubDepartment, float salary) {
        assertThrows(EmployeeAlreadyAddedException.class,
                () -> testEmployeeBookService.addEmployeeToBook(inpName, inpLastName, inpSubDepartment, salary));
    }

    @ParameterizedTest
    @MethodSource("getParamsForTests")
    void addEmployeeToBookTest(String inpName, String inpLastName, short inpSubDepartment, float salary) {
        Employee inpWorker = new Employee(inpName, inpLastName, inpSubDepartment, salary);
        String expectedWorkerStr = inpWorker.toString();

        String[] methodResultStr = testEmployeeBookService.addEmployeeToBook(inpName, inpLastName, inpSubDepartment, salary).split(";");
        assertEquals(true, methodResultStr[0].endsWith(expectedWorkerStr));
    }

    @ParameterizedTest
    @MethodSource("getParamsForTests")
    void deleteEmployeeFromBookTestBadRequestException(String inpName, String inpLastName) {
        assertThrows(BadRequestException.class,
                () -> testEmployeeBookService.deleteEmployeeFromBook(inpName, inpLastName));
    }

    @ParameterizedTest
    @MethodSource("getParamsForTests")
    void deleteEmployeeFromBookTestEmployeeNotFound(String inpName, String inpLastName) {
        assertThrows(EmployeeNotFoundException.class,
                () -> testEmployeeBookService.deleteEmployeeFromBook(inpName, inpLastName));
    }

    @ParameterizedTest
    @MethodSource("getParamsForTests")
    void deleteEmployeeFromBookTest(String inpName, String inpLastName) {
        Employee inpWorker = new Employee(inpName, inpLastName, (short) 1, 0.00f);
        String expectedStr = String.format("Сотрудник %s был удален", inpWorker.toString());
        String[] methodResultStr = testEmployeeBookService.deleteEmployeeFromBook(inpName, inpLastName).split(";");
        assertEquals(expectedStr, methodResultStr[0]);
    }

    @ParameterizedTest
    @MethodSource("getParamsForTests")
    void findEmployeeTestBadRequestException(String inpName, String inpLastName) {
        assertThrows(BadRequestException.class,
                () -> testEmployeeBookService.findEmployee(inpName, inpLastName));
    }

    @ParameterizedTest
    @MethodSource("getParamsForTests")
    void findEmployeeTestEmployeeNotFound(String inpName, String inpLastName) {
        assertThrows(EmployeeNotFoundException.class,
                () -> testEmployeeBookService.findEmployee(inpName, inpLastName));
    }

    @ParameterizedTest
    @MethodSource("getParamsForTests")
    void findEmployeeTest(String inpName, String inpLastName) {
        Employee inpWorker = new Employee(inpName, inpLastName, (short) 1, 10_000.00f);
        String expectedStr = String.format("Найден сотрудник %s", inpWorker.toString());
        String[] methodResultStr = testEmployeeBookService.findEmployee(inpName, inpLastName).split(";");
        assertEquals(expectedStr, methodResultStr[0]);
    }
}
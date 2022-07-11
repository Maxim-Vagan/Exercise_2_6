package ru.maxvagan.services.impls;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.maxvagan.mainclasses.Employee;
import ru.maxvagan.services.DepartmentService;
import ru.maxvagan.services.EmployeeBookService;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {
    private List<Employee> dummyTestStaff;

    @Mock
    private EmployeeBookService testEmployeeBookService;

    private DepartmentService departmentService;

    private static Stream<Arguments> getParamsForTests() {
        return Stream.of(Arguments.of((short) 1),
                Arguments.of((short) 2));
    }

    @BeforeEach
    void setUp() {
        dummyTestStaff = Lists.newArrayList(new Employee( "Иван", "Михаилович", (short)1, 10_000f),
                new Employee( "Артем", "Сидорович", (short)1, 15_000f));
        departmentService = new DepartmentServiceImpl(testEmployeeBookService);
    }

    @ParameterizedTest
    @MethodSource("getParamsForTests")
    void getMaxSalaryEmployeeInSubDepTestPass(short inpSubDepID) {
        assertNotNull(testEmployeeBookService);
        Mockito.when(testEmployeeBookService.getBookOfStaff()).thenReturn(dummyTestStaff);
        assertEquals(dummyTestStaff.get(1).toString(), departmentService.getMaxSalaryEmployeeInSubDep(inpSubDepID));
    }

    @ParameterizedTest
    @MethodSource("getParamsForTests")
    void getMaxSalaryEmployeeInSubDepTestFail(short inpSubDepID) {
        assertNotNull(testEmployeeBookService);
        Mockito.when(testEmployeeBookService.getBookOfStaff()).thenReturn(dummyTestStaff);
        assertEquals(dummyTestStaff.get(0).toString(), departmentService.getMaxSalaryEmployeeInSubDep(inpSubDepID));
    }

    @ParameterizedTest
    @MethodSource("getParamsForTests")
    void getMinSalaryEmployeeInSubDepTestPass(short inpSubDepID) {
        assertNotNull(testEmployeeBookService);
        Mockito.when(testEmployeeBookService.getBookOfStaff()).thenReturn(dummyTestStaff);
        assertEquals(dummyTestStaff.get(1).toString(), departmentService.getMinSalaryEmployeeInSubDep(inpSubDepID));
    }

    @ParameterizedTest
    @MethodSource("getParamsForTests")
    void getMinSalaryEmployeeInSubDepTestFail(short inpSubDepID) {
        assertNotNull(testEmployeeBookService);
        Mockito.when(testEmployeeBookService.getBookOfStaff()).thenReturn(dummyTestStaff);
        assertEquals(dummyTestStaff.get(0).toString(), departmentService.getMinSalaryEmployeeInSubDep(inpSubDepID));
    }

}
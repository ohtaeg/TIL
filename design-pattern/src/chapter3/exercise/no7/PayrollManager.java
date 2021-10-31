package chapter3.exercise.no7;

import chapter3.exercise.no5.Employee;
import java.util.ArrayList;
import java.util.List;

public class PayrollManager {
    private List<Employee> employees = new ArrayList<>();

    public void writeEmployeePay(PayPrinter payPrinter) {
        payPrinter.print(employees);
    }
}

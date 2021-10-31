package chapter3.exercise.no7;

import chapter3.exercise.no5.Employee;
import java.util.Iterator;
import java.util.List;

public class PayConsolePrinter implements PayPrinter {

    @Override
    public void print(List<Employee> employees) {
        Iterator<Employee> iterator = employees.iterator();
        while (iterator.hasNext()) {
            Employee cursor = iterator.next();
            System.out.println(cursor.calculatePay());
        }
    }
}

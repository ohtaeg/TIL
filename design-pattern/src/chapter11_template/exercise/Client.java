package chapter11_template.exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Client {

    public static void main(String[] args) {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("홍길동", 150));
        customers.add(new Customer("우수한", 350));
        customers.add(new Customer("부족한", 50));
        customers.add(new Customer("훌륭한", 450));
        customers.add(new Customer("최고의", 550));

        ReportGenerator reportGenerator = new ReportGenerator(customers);

        String result = reportGenerator.generate(customers1 -> customers1,
            (name, point) -> String.format("%s: %d\n", name, point));
        System.out.println(result);

        result = reportGenerator.generate(customers1 -> customers1.stream()
                                                                 .filter(customer -> customer.getPoint() >= 100)
                                                                 .collect(Collectors.toList())
            , (name, point) -> {
                if (point >= 100) {
                    return String.format("%s: %d\n", name, point);
                }
                return "";
            });
        System.out.println(result);
    }
}

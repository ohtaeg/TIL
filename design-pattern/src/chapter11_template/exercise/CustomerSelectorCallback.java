package chapter11_template.exercise;

import java.util.List;

@FunctionalInterface
public interface CustomerSelectorCallback {
    List<Customer> select(List<Customer> customers);
}

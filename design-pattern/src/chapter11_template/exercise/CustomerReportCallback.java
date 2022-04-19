package chapter11_template.exercise;

@FunctionalInterface
public interface CustomerReportCallback {

    String reportCustomer(String name, int point);

}

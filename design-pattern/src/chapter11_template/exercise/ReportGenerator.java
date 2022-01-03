package chapter11_template.exercise;

import java.util.List;

public class ReportGenerator {

    private List<Customer> customers;

    public ReportGenerator(final List<Customer> customers) {
        this.customers = customers;
    }

    public String generate(CustomerSelectorCallback customerSelector, CustomerReportCallback customerReportCallback) {
        final List<Customer> select = customerSelector.select(customers);

        String report = String.format("고객 수 : %d명\n", select.size());
        for (int i = 0; i < select.size(); i++) {
            Customer customer = select.get(i);
            report += customerReportCallback.reportCustomer(customer.getName(), customer.getPoint());
        }
        return report;
    }
}

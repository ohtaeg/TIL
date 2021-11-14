package chapter5_strategy.exercise.no3.discount;

import chapter5_strategy.exercise.no3.Book;

public interface DiscountStrategy {
    int discount(Book book);
}

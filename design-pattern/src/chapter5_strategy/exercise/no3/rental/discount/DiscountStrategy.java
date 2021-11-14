package chapter5_strategy.exercise.no3.rental.discount;

import chapter5_strategy.exercise.no3.rental.Book;

public interface DiscountStrategy {
    int discount(Book book);
}

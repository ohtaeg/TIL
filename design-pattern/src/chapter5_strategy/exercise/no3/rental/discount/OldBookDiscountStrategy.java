package chapter5_strategy.exercise.no3.rental.discount;

import chapter5_strategy.exercise.no3.rental.Book;

public class OldBookDiscountStrategy implements DiscountStrategy{

    @Override
    public int discount(final Book book) {
        return book.getPrice() - 1000;
    }
}

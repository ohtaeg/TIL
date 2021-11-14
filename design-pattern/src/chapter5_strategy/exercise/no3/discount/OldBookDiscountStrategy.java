package chapter5_strategy.exercise.no3.discount;

import chapter5_strategy.exercise.no3.Book;

public class OldBookDiscountStrategy implements DiscountStrategy{

    @Override
    public int discount(final Book book) {
        return book.getPrice() - 1000;
    }
}

package chapter5_strategy.exercise.no3.rental.discount;

import chapter5_strategy.exercise.no3.rental.Book;

public class MemberDisCountStrategy implements DiscountStrategy{

    @Override
    public int discount(final Book book) {
        return (int) (book.getPrice() * 0.8);
    }
}

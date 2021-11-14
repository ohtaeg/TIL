package chapter5_strategy.exercise.no3.discount;

import chapter5_strategy.exercise.no3.Book;

public class MemberDisCountStrategy implements DiscountStrategy{

    @Override
    public int discount(final Book book) {
        return (int) (book.getPrice() * 0.8);
    }
}

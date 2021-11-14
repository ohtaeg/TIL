package chapter5_strategy.exercise.no3.rental.discount;

import chapter5_strategy.exercise.no3.rental.Book;
import chapter5_strategy.exercise.no3.rental.Member;

public class SimpleDiscountFactory implements DiscounterFactory{

    @Override
    public DiscountStrategy getDisCounter(final Member member, final Book book) {
        if (book.getPublishedYear() >= 10) {
            return new OldBookDiscountStrategy();
        }

        if (member.getAccumulatedRentalPrice() >= 10000) {
            return new MemberDisCountStrategy();
        }
        return (book1) -> book.getPrice();
    }
}

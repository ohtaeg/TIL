package chapter5_strategy.exercise.no3.rental.discount;

import chapter5_strategy.exercise.no3.rental.Book;
import chapter5_strategy.exercise.no3.rental.Member;

public interface DiscounterFactory {
    DiscountStrategy getDisCounter(Member member, Book book);
}

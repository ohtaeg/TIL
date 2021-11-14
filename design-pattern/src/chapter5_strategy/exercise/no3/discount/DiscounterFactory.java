package chapter5_strategy.exercise.no3.discount;

import chapter5_strategy.exercise.no3.Book;
import chapter5_strategy.exercise.no3.Member;

public interface DiscounterFactory {
    DiscountStrategy getDisCounter(Member member, Book book);
}

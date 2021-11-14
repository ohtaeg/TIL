package chapter5_strategy.exercise.no3;

import chapter5_strategy.exercise.no3.discount.DiscountStrategy;
import chapter5_strategy.exercise.no3.discount.DiscounterFactory;

public class Member {
    private String name;
    private int accumulatedRentalPrice;
    private DiscounterFactory discounterFactory;

    public Member(final String name, final int accumulatedRentalPrice) {
        this.name = name;
        this.accumulatedRentalPrice = accumulatedRentalPrice;
    }

    public void buyBook(Book... books) {
        for (Book book : books) {
            DiscountStrategy discountStrategy = discounterFactory.getDisCounter(this, book);
            this.accumulatedRentalPrice += discountStrategy.discount(book);
        }
    }

    public void setDiscounterFactory(final DiscounterFactory discounterFactory) {
        this.discounterFactory = discounterFactory;
    }

    public int getAccumulatedRentalPrice() {
        return accumulatedRentalPrice;
    }
}

package errors;

import InMemoryDatabase.InMemoryDatabase;

public class InsufficientAmountOfMoney extends Exception {
    public InsufficientAmountOfMoney() {
        super("Insufficient Amount Of Money");
    }
}

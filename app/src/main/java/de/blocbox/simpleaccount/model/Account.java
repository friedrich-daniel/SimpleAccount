package de.blocbox.simpleaccount.model;

public interface Account {
    int getUid();
    String getFirstName();
    String getLastName();
    AccountType getAccountType();
}

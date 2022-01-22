package de.blocbox.simpleaccount.model;

public interface Account {
    int getUid();
    String getName();
    String getDescription();
    AccountType getAccountType();
}

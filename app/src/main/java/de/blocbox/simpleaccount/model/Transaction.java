package de.blocbox.simpleaccount.model;

import java.util.Date;

public interface Transaction {
    int getUid();
    int getAccountUid();
    double getAmount();
    String getDescription();
    Date getDate();
}

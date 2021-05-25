package de.blocbox.simpleaccount.db.entity;

import androidx.room.Embedded;
import androidx.room.Query;
import androidx.room.Relation;

import java.util.List;

public class AccountWithTransactionEntity implements Comparable<AccountWithTransactionEntity> {
    @Embedded
    public AccountEntity accountEntity;
    @Relation(
            parentColumn = "uid",
            entityColumn = "account_uid"
    )
    public List<TransactionEntity> transactionEntities;

    @Override
    public int compareTo(AccountWithTransactionEntity o) {
        String string1 = accountEntity.getFirstName() + accountEntity.getLastName();
        String string2 = o.accountEntity.getFirstName() + o.accountEntity.getLastName();

        return  string1.compareTo( string2 );
    }
}

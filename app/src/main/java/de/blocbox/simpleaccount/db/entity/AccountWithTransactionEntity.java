package de.blocbox.simpleaccount.db.entity;

import androidx.room.Embedded;
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
        return  accountEntity.getName().compareTo( o.accountEntity.getName() );
    }
}

package de.blocbox.simpleaccount.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import de.blocbox.simpleaccount.model.Account;
import de.blocbox.simpleaccount.model.AccountType;

@Entity(tableName = "accounts")
public class AccountEntity implements Account {

    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "accountType")
    private AccountType accountType;

    @Override
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public AccountType getAccountType() {
        return accountType;
    }
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountEntity()
    {
    }

    public AccountEntity(Account account)
    {
        this.uid = account.getUid();
        this.name = account.getName();
        this.description = account.getDescription();
        this.accountType = account.getAccountType();
    }

    @Ignore
    public AccountEntity(String name, String description, AccountType accountType) {
        this.name = name;
        this.description = description;
        this.accountType = accountType;
    }

    @Ignore
    public AccountEntity(int uid, String name, String description, AccountType accountType) {
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.accountType = accountType;
    }
}
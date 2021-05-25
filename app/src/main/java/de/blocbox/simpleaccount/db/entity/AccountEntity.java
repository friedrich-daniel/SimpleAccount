package de.blocbox.simpleaccount.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import de.blocbox.simpleaccount.model.AccountType;
import de.blocbox.simpleaccount.model.Account;

import java.util.Comparator;

@Entity(tableName = "accounts")
public class AccountEntity implements Account {

    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "first_name")
    private String firstName;
    @ColumnInfo(name = "last_name")
    private String lastName;
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
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.accountType = account.getAccountType();
    }

    @Ignore
    public AccountEntity(String firstName, String lastName, AccountType accountType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountType = accountType;
    }

    @Ignore
    public AccountEntity(int uid, String firstName, String lastName, AccountType accountType) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountType = accountType;
    }

    public static Comparator<AccountEntity> CompareFirstName = new Comparator<AccountEntity>() {
        public int compare(AccountEntity s1, AccountEntity s2) {
            return s1.firstName.compareTo( s2.firstName );
        }
    };
}
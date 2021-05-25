package de.blocbox.simpleaccount.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

import de.blocbox.simpleaccount.model.Transaction;

@Entity(tableName = "transactions")
public class TransactionEntity implements Comparable<TransactionEntity> {

    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "account_uid")
    private int accountUid;
    @ColumnInfo(name = "amount")
    private double amount;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "date")
    private Date date;

    //@Override
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }

    //@Override
    public int getAccountUid() {
        return accountUid;
    }
    public void setAccountUid(int accountUid) {
        this.accountUid = accountUid;
    }

    //@Override
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    //@Override
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    //@Override
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public TransactionEntity()
    {
    }

    public TransactionEntity(Transaction transaction)
    {
        this.uid = transaction.getUid();
        this.accountUid = transaction.getAccountUid();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
    }

    @Ignore
    public TransactionEntity(int accountUid, double amount, String description, Date date)
    {
        this.accountUid = accountUid;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    @Ignore
    public TransactionEntity(int uid, int accountUid, double amount, String description, Date date)
    {
        this.uid = uid;
        this.accountUid = accountUid;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    @Override
    public int compareTo(TransactionEntity transactionEntity) {
        return this.date.compareTo( transactionEntity.date );
     }
}

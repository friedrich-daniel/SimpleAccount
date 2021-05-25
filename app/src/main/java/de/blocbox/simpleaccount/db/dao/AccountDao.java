package de.blocbox.simpleaccount.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import de.blocbox.simpleaccount.db.entity.AccountEntity;
import de.blocbox.simpleaccount.db.entity.AccountWithTransactionEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.ABORT;

@Dao
public interface AccountDao {

    @RawQuery
    int checkpoint(SupportSQLiteQuery supportSQLiteQuery);

    @Query("SELECT * FROM accounts where uid = :uid")
    LiveData<AccountEntity> loadAccount(final int uid);

    @Transaction
    @Query("SELECT * FROM accounts where uid = :uid")
    LiveData<AccountWithTransactionEntity> loadAccountWithTransactions(final int uid);

    @Transaction
    @Query("SELECT *  FROM accounts")
    LiveData<List<AccountWithTransactionEntity>> loadAccountsWithTransactions();

    @Insert(onConflict = ABORT)
    void insertPerson(AccountEntity personEntity);

    @Update
    void updatePerson(AccountEntity personEntity);

    @Delete
    void deletePerson(AccountEntity personEntity);

}

package de.blocbox.simpleaccount.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import de.blocbox.simpleaccount.db.entity.TransactionEntity;

import static androidx.room.OnConflictStrategy.ABORT;

@Dao
public interface TransactionDao {

    @Query("SELECT * FROM transactions where uid = :transactionUid")
    LiveData<TransactionEntity> loadTransaction(final int transactionUid);

    @Insert(onConflict = ABORT)
    void insertTransaction(TransactionEntity transactionEntity);

    @Update
    void updateTransaction(TransactionEntity transactionEntity);

    @Delete
    void deleteTransaction(TransactionEntity transactionEntity);
}

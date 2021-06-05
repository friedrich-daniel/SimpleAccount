package de.blocbox.simpleaccount;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import de.blocbox.simpleaccount.db.AppDatabase;
import de.blocbox.simpleaccount.db.entity.AccountEntity;
import de.blocbox.simpleaccount.db.entity.AccountWithTransactionEntity;
import de.blocbox.simpleaccount.db.entity.TransactionEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DataRepository {

    private static DataRepository sDataRepositoryInstance;
    private final AppDatabase mAppDatabase;
    private final Executor mDiskIO;
    private boolean checkpointInProgress = false;

    private DataRepository(final AppDatabase appDatabase) {
        mAppDatabase = appDatabase;
        mDiskIO =  Executors.newSingleThreadExecutor();
    }

    public static DataRepository getInstance(final AppDatabase appDatabase) {
        if (sDataRepositoryInstance == null)
        {
            synchronized (DataRepository.class)
            {
                if (sDataRepositoryInstance == null) {
                    sDataRepositoryInstance = new DataRepository(appDatabase);
                }
            }
        }
        return sDataRepositoryInstance;
    }

    public static void destroyInstance()
    {
        if (sDataRepositoryInstance != null)
        {
            synchronized (DataRepository.class) {
                sDataRepositoryInstance = null;
            }
        }
    }

    public void checkpoint() {
        checkpointInProgress = true;
        mDiskIO.execute( () -> {
            mAppDatabase.accountDao().checkpoint( new SimpleSQLiteQuery( "pragma wal_checkpoint(full)" ) );
            checkpointInProgress = false;
        } );
    }

    public boolean getCheckpointInProgress()
    {
        return checkpointInProgress;
    }

    public LiveData<AccountWithTransactionEntity> getAccountWithTransactions(int accountUid) {
        return mAppDatabase.accountDao().loadAccountWithTransactions(accountUid);
    }

    public LiveData<List<AccountWithTransactionEntity>> getAccountsWithTransactions() {
        return mAppDatabase.accountDao().loadAccountsWithTransactions();
    }

    public LiveData<AccountEntity> getAccount(int uid) {
        return mAppDatabase.accountDao().loadAccount( uid );
    }

    public void insertAccount(final AccountEntity accountEntity) {
        mDiskIO.execute( () -> mAppDatabase.accountDao().insertPerson( accountEntity ) );
    }

    public void updateAccount(final AccountEntity accountEntity) {
        mDiskIO.execute( () -> mAppDatabase.accountDao().updatePerson( accountEntity ) );
    }

    public void deleteAccount(final AccountEntity accountEntity){
        mDiskIO.execute( () -> {
            //try{
            //    Thread.sleep(500);
            //} catch (InterruptedException ignored) {
            //}
            mAppDatabase.accountDao().deletePerson( accountEntity );
        } );
    }

    public LiveData<TransactionEntity> getTransaction(int transactionUid) {
        return mAppDatabase.transactionDao().loadTransaction( transactionUid );
    }

    public void insertTransaction(final TransactionEntity transactionEntity) {
        mDiskIO.execute( () -> mAppDatabase.transactionDao().insertTransaction( transactionEntity ) );
    }

    public void updateTransaction(final TransactionEntity transactionEntity){
        mDiskIO.execute( () -> mAppDatabase.transactionDao().updateTransaction(  transactionEntity ) );
    }

    public void deleteTransaction(final TransactionEntity transactionEntity){
        mDiskIO.execute( () -> mAppDatabase.transactionDao().deleteTransaction(  transactionEntity ) );
    }
}

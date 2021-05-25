package de.blocbox.simpleaccount.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import de.blocbox.simpleaccount.DataRepository;
import de.blocbox.simpleaccount.SimpleAccountApp;
import de.blocbox.simpleaccount.db.entity.AccountEntity;
import de.blocbox.simpleaccount.db.entity.AccountWithTransactionEntity;
import de.blocbox.simpleaccount.db.entity.TransactionEntity;

public class AccountViewModel extends AndroidViewModel {

    private final DataRepository mDataRepository;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        mDataRepository = ((SimpleAccountApp)application).getDataRepository();
    }

    public LiveData<AccountWithTransactionEntity> getLiveDataAccountWithTransactions(final int accountUid) {
        return mDataRepository.getAccountWithTransactions(accountUid);
    }

    public LiveData<AccountEntity> getLiveDataAccount(final int uid) {
        return mDataRepository.getAccount( uid );
    }

    public void addPerson(AccountEntity personEntity) {
        mDataRepository.insertAccount( personEntity );
    }

    public void updatePerson(AccountEntity personEntity) {
        mDataRepository.updateAccount( personEntity );
    }

    public void addTransaction(TransactionEntity transactionEntity) {
        mDataRepository.insertTransaction( transactionEntity );
    }

    public void deleteTransaction(TransactionEntity transactionEntity) {
        mDataRepository.deleteTransaction( transactionEntity );
    }
}

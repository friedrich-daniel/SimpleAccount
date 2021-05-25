package de.blocbox.simpleaccount.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import de.blocbox.simpleaccount.DataRepository;
import de.blocbox.simpleaccount.SimpleAccountApp;
import de.blocbox.simpleaccount.db.entity.TransactionEntity;

public class TransactionViewModel extends AndroidViewModel
{
    private final DataRepository mDataRepository;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        mDataRepository = ((SimpleAccountApp)application).getDataRepository();
    }

    public LiveData<TransactionEntity> getTransaction(int transactionUid) {
        return mDataRepository.getTransaction( transactionUid );
    }

    public void addTransaction(TransactionEntity transactionEntity) {
        mDataRepository.insertTransaction( transactionEntity );
    }

    public void updateTransaction(TransactionEntity transactionEntity) {
        mDataRepository.updateTransaction( transactionEntity );
    }

}

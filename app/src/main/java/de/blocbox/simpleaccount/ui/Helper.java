package de.blocbox.simpleaccount.ui;

import android.graphics.drawable.Drawable;

import java.util.List;
import java.util.Locale;

import de.blocbox.simpleaccount.R;
import de.blocbox.simpleaccount.db.entity.TransactionEntity;
import de.blocbox.simpleaccount.model.AccountType;

public class Helper {

    public static int GetDrawableByAccountType(AccountType accountType)
    {
        int drawable;
        switch (accountType) {
            case FEMALE:
                drawable = R.drawable.ic_face_woman;
                break;
            case MALE:
                drawable = R.drawable.ic_face_man;
                break;
            case GROUP:
            default:
                drawable = R.drawable.ic_group_black_24dp;
                break;
        }
        return drawable;
    }

    public static String GetSumOfTransitions(List<TransactionEntity> transactionEntities)
    {
        double sum = 0;
        for (TransactionEntity transition :transactionEntities) {
            sum += transition.getAmount();
        }
        return String.format( Locale.ENGLISH,"%.2f", sum);
    }
}

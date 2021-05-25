package de.blocbox.simpleaccount.db.converter;

import androidx.room.TypeConverter;

import de.blocbox.simpleaccount.model.AccountType;

public class AccountTypeConverter {

    static final int GROUP = 0;
    static final int MALE = 1;
    static final int FEMALE = 2;

    @TypeConverter
    public static int fromAccountTye(AccountType accountType)
    {
        int i;
        switch (accountType){
            case FEMALE:
                i = FEMALE;
                break;
            case MALE:
                i = MALE;
                break;
            case GROUP:
            default:
                i = GROUP;
                break;
        }
        return i;
    }

    @TypeConverter
    public static AccountType toAccountType(int i)
    {
        AccountType accountType;
        switch (i){
            case FEMALE:
                accountType = AccountType.FEMALE;
                break;
            case MALE:
                accountType = AccountType.MALE;
                break;
            case GROUP:
            default:
                accountType = AccountType.GROUP;
                break;
        }
        return accountType;
    }
}

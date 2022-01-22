package de.blocbox.simpleaccount.db.converter;

import androidx.room.TypeConverter;

import de.blocbox.simpleaccount.model.AccountType;

public class AccountTypeConverter {

    static final int DEFAULT = 0;
    static final int GROUP = 1;
    static final int MALE = 2;
    static final int FEMALE = 3;

    @TypeConverter
    public static int fromAccountTye(AccountType accountType)
    {
        int i;
        switch (accountType){
            case GROUP:
                i = GROUP;
                break;
            case MALE:
                i = MALE;
                break;
            case FEMALE:
                i = FEMALE;
                break;
            case DEFAULT:
            default:
                i = DEFAULT;
                break;
        }
        return i;
    }

    @TypeConverter
    public static AccountType toAccountType(int i)
    {
        AccountType accountType;
        switch (i){
            case GROUP:
                accountType = AccountType.GROUP;
                break;
            case MALE:
                accountType = AccountType.MALE;
                break;
            case FEMALE:
                accountType = AccountType.FEMALE;
                break;
            case DEFAULT:
            default:
                accountType = AccountType.DEFAULT;
                break;
        }
        return accountType;
    }
}

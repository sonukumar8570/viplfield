package in.visibleinfotech.viplfieldapplications.field_survey.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "account_master")
public class AccountMaster {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "accountId")
    String accountId;
    @ColumnInfo(name = "placeID")
    String placeId;
    @ColumnInfo(name = "accountName")
    String accountName;
    @ColumnInfo(name = "contactId")
    String contactId;

    public AccountMaster(@NonNull String accountId, String placeId, String accountName, String contactId) {
        this.accountId = accountId;
        this.placeId = placeId;
        this.accountName = accountName;
        this.contactId = contactId;
    }

    @NonNull
    public String getAccountId() {
        return accountId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getContactId() {
        return contactId;
    }
}

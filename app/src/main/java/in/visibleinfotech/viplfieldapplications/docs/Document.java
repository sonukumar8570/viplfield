package in.visibleinfotech.viplfieldapplications.docs;

import java.io.File;

public class Document {
    File image;
    boolean bitmap;
    String docId;
    private String accountId, villageId, docTypeCode, docTypeName;

    public Document(String accountId, String villageId, String docTypeCode, String docTypeName, File image) {
        this.accountId = accountId;
        this.villageId = villageId;
        this.docTypeCode = docTypeCode;
        this.docTypeName = docTypeName;
        this.image = image;
    }

    public Document(String accountId, String villageId, String docTypeCode, boolean image, String docId) {
        this.accountId = accountId;
        this.villageId = villageId;
        this.docTypeCode = docTypeCode;
        this.bitmap = image;
        this.docId = docId;
    }

    public File getImage() {
        return image;
    }

    public boolean getBitmap() {
        return bitmap;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getVillageId() {
        return villageId;
    }

    public String getDocTypeCode() {
        return docTypeCode;
    }

    public String getDocTypeName() {
        return docTypeName;
    }

    public String getDocId() {
        return docId;
    }
}

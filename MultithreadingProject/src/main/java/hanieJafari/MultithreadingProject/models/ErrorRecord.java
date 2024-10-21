package hanieJafari.MultithreadingProject.models;

public class ErrorRecord {
    private String recordNumber;
    private String errorCode;
    private String errorDescription;

    public ErrorRecord(String recordNumber, String errorCode, String errorDescription) {
        this.recordNumber = recordNumber;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
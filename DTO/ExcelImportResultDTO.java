package DTO;

import java.util.ArrayList;
import java.util.List;

public class ExcelImportResultDTO {
    private boolean success;
    private int totalRows;
    private int successRows;
    private final ArrayList<String> errors;

    public ExcelImportResultDTO() {
        this.success = false;
        this.totalRows = 0;
        this.successRows = 0;
        this.errors = new ArrayList<>();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getSuccessRows() {
        return successRows;
    }

    public void setSuccessRows(int successRows) {
        this.successRows = successRows;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}

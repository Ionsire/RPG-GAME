package com.example.rpg;

public class UploadConfig {

    private String fileType; // Image or Video ("img", "vid")
    private String dateName;

    public UploadConfig(){

    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String newType) {
        this.fileType = newType;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }
}

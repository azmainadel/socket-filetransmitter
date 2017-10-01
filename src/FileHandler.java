/**
 * Created by xyntherys on 9/30/17.
 */
public class FileHandler {
    private String fileName;
    private long fileSize;
    private String fileID;
    private String fileLocation;

    public String getFileName() {
        return fileLocation.substring(fileLocation.lastIndexOf("/") + 1, fileLocation.length());
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public void generateFileID(String fileName, int studentID){
        this.fileID = "FILE-" + String.valueOf(studentID) + getFileName().toUpperCase();
    }

    public FileHandler() {
    }
}

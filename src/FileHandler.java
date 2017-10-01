import java.io.File;

/**
 * Created by xyntherys on 9/30/17.
 */
public class FileHandler {
    private File file;
    private String fileName;
    private long fileSize;
    private String fileID;
    private String fileLocation;

    public String getFileName() {
        return fileLocation.substring(fileLocation.lastIndexOf("/") + 1, fileLocation.length());
//        return this.file.getName();
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return this.file.length();
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(int studentID) {
        this.fileID = "FILE-" + String.valueOf(studentID) + getFileName().toUpperCase();
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public FileHandler(String fileLocation) {
        this.file = new File(fileLocation);
    }

    public FileHandler() {
    }
}

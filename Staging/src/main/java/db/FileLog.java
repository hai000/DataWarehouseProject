package db;

import java.sql.Date;

public class FileLog {
    private long id;
    private long config_id;
    private String status;
    private Date createdAt;
    private String file_data;

    public FileLog(long id, long config_id, String status, Date createdAt, String file_data) {
        this.id = id;
        this.config_id = config_id;
        this.status = status;
        this.createdAt = createdAt;
        this.file_data = file_data;
    }

    public FileLog() {
    }

    public FileLog(long config_id, String status) {
        this.config_id = config_id;
        this.status = status;
    }

    public void setFile_data(String file_data) {
        this.file_data = file_data;
    }

    public String getFile_data() {
        return file_data;
    }


    public long getId() {
        return id;
    }

    public long getConfig_id() {
        return config_id;
    }

    public String getStatus() {
        return status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setConfig_id(long config_id) {
        this.config_id = config_id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "FileLog{" +
                "id=" + id +
                ", config_id=" + config_id +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", file_data='" + file_data + '\'' +
                '}';
    }
}

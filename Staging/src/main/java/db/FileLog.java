package db;

import java.sql.Date;
import java.sql.Timestamp;

public class FileLog {
    private long id;
    private long config_id;
    private String status;
    private Date createdAt;
    private String file_data;
    private Timestamp updatedAt;
    private Timestamp reloadedAt;
    private int count=0;
    private int file_size_kb=0;


    public FileLog(long id, long config_id, String status, Date createdAt, String file_data, Timestamp updatedAt, Timestamp reloadedAt, int count, int file_size_kb) {
        this.id = id;
        this.config_id = config_id;
        this.status = status;
        this.createdAt = createdAt;
        this.file_data = file_data;
        this.updatedAt = updatedAt;
        this.reloadedAt = reloadedAt;
        this.count = count;
        this.file_size_kb = file_size_kb;
    }

    public FileLog() {
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getReloadedAt() {
        return reloadedAt;
    }

    public void setReloadedAt(Timestamp reloadedAt) {
        this.reloadedAt = reloadedAt;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFile_size_kb() {
        return file_size_kb;
    }

    public void setFile_size_kb(int file_size_kb) {
        this.file_size_kb = file_size_kb;
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
                ", updatedAt=" + updatedAt +
                ", reloadedAt=" + reloadedAt +
                ", count=" + count +
                ", file_size_kb=" + file_size_kb +
                '}';
    }
}

package db;

import java.sql.Date;

public class DataSource {
    private long id;
    private String name;
    private String address;
    private String file_location;
    private String staging_table;
    private String file_name_format;
    private Date createdAt;

    public DataSource(long id, String name, String address, String file_location, String staging_table, String file_name_format, Date createdAt) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.file_location = file_location;
        this.staging_table = staging_table;
        this.file_name_format = file_name_format;
        this.createdAt = createdAt;
    }

    public DataSource() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getFile_location() {
        return file_location;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getStaging_table() {
        return staging_table;
    }

    public void setStaging_table(String staging_table) {
        this.staging_table = staging_table;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFile_location(String file_location) {
        this.file_location = file_location;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getFile_name_format() {
        return file_name_format;
    }

    public void setFile_name_format(String file_name_format) {
        this.file_name_format = file_name_format;
    }

    @Override
    public String toString() {
        return "DataSource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", file_location='" + file_location + '\'' +
                ", staging_table='" + staging_table + '\'' +
                ", file_name_format='" + file_name_format + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

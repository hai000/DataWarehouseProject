package jdbiInterface;

import db.FileLog;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterBeanMapper(FileLog.class)
public interface FileLogInterface {
    @SqlQuery("SELECT file_log.id, file_log.config_id, file_log.status, file_log.createdAt, file_log.file_data " +
            "FROM file_log JOIN file_config ON file_log.config_id = file_config.id " +
            "WHERE file_log.createdAt = CURDATE() AND file_config.name LIKE :dataSource")
    FileLog getTodayLogBySource(@Bind("dataSource") String dataSource);
    @SqlQuery("SELECT * FROM file_log " +
            "WHERE file_log.status = :status " +
            "AND file_log.config_id = :config_id " +
            "ORDER BY file_log.createdAt ASC " +
            "LIMIT 1")
    FileLog getBySourceAndStatus(@Bind("config_id") long config_id, @Bind("status") String status);
    @SqlQuery("SELECT * FROM file_log " +
            "WHERE file_log.status = :status " +
            "AND file_log.config_id = :config_id " +
            "AND file_log.createdAt = :date")
    FileLog getBySourceAndStatusAndDate(@Bind("config_id") long config_id, @Bind("status") String status, @Bind("date")String date);
    @SqlQuery("SELECT * FROM file_log " +
            "WHERE file_log.config_id = :config_id " +
            "AND file_log.createdAt = :date")
    FileLog getBySourceAndDate(@Bind("config_id") long config_id, @Bind("date") String date);
    @SqlUpdate("Update file_log set status =:status where createdAt = curdate() and config_id=:config_id")
    int updateStatusBySource(@Bind("status") String status, @Bind("config_id") long config_id);
    @SqlUpdate("Update file_log set status =:status where id=:id")
    int updateStatusByID(@Bind("status") String status, @Bind("id") long id);
    @SqlUpdate("insert into file_log (config_id, status) " +
            "values(:config_id, :status)")
    int addLog(@BindBean() FileLog fileLog);
    @SqlUpdate("Update file_log " +
            "set file_data =:file_data , updatedAt=:updatedAt, reloadedAt=:reloadedAt, count=:count, file_size_kb=:file_size_kb, status=:status " +
            "where createdAt = curdate() and config_id=:config_id")
    int updateTodayLog(@BindBean() FileLog fileLog, @Bind("config_id") long config_id);

    @SqlUpdate("Update file_log " +
            "set file_data =:file_data , updatedAt=:updatedAt, reloadedAt=:reloadedAt, count=:count, file_size_kb=:file_size_kb, status=:status " +
            "where id=:id")
    int updateLogById(@BindBean() FileLog fileLog);


}

package jdbiInterface;

import db.DataSource;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@RegisterBeanMapper(DataSource.class)
public interface DataSourceInterface {
     @SqlQuery("Select * from file_config where file_config.name like :source")
     DataSource getDataSource(@Bind("source") String source);
     @SqlQuery("Select * from file_config")
     List<DataSource> getAllDataSource();
}

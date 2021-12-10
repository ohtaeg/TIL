package chapter8_command.lecture;

import java.util.Map;
import javax.sql.DataSource;

public class SimpleJdbcInsert {

    private DataSource dataSource;

    public SimpleJdbcInsert(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public SimpleJdbcInsert withTableName(final String command) {
        return this;
    }

    public SimpleJdbcInsert usingGeneratedKeyColumns(final String id) {
        return this;
    }

    public void execute(final Map<String, Object> data) {
    }
}

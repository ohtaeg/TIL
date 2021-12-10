package chapter8_command.lecture;

import chapter8_command.lecture.command.Command;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

public class CommandInSpring {
    private DataSource dataSource;

    public CommandInSpring(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(Command command) {
        // JDBC Template
        // insert = 하나의 커맨드 역할을 한다.
        SimpleJdbcInsert insert = new SimpleJdbcInsert(dataSource).withTableName("command").usingGeneratedKeyColumns("id");

        Map<String, Object> data = new HashMap<>();
        data.put("name", command.getClass().getSimpleName());
        data.put("when", LocalDateTime.now());
        insert.execute(data);
    }
}

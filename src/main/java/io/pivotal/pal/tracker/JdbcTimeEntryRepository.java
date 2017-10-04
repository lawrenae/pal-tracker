package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private JdbcTemplate template;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "insert into time_entries(project_id, user_id, date, hours) VALUES (?, ?, ?, ?)";
        
        template.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
                    ps.setLong(1, timeEntry.getProjectId());
                    ps.setLong(2, timeEntry.getUserId());
                    ps.setDate(3, Date.valueOf(timeEntry.getDate()));
                    ps.setInt(4, timeEntry.getHours());
                    return ps;
                }, keyHolder);

        return find(keyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(Long id) {
        try {
            return this.template.queryForObject(
                    "select * from time_entries where id = ?",
                    new Object[]{id},
                    (rs, rowNum) -> new TimeEntry(
                            rs.getLong("id"),
                            rs.getLong("project_id"),
                            rs.getLong("user_id"),
                            rs.getDate("date").toLocalDate(),
                            rs.getInt("hours"))
            );
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override
    public List<TimeEntry> list() {
        return this.template.query(
                "select * from time_entries",
                (rs, rowNum) -> new TimeEntry(
                        rs.getLong("id"),
                        rs.getLong("project_id"),
                        rs.getLong("user_id"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("hours"))
        );
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "update time_entries set project_id = ?, user_id = ?, date = ?, hours = ? where id = ?";
        template.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
                    ps.setLong(1, timeEntry.getProjectId());
                    ps.setLong(2, timeEntry.getUserId());
                    ps.setDate(3, Date.valueOf(timeEntry.getDate()));
                    ps.setInt(4, timeEntry.getHours());
                    ps.setLong(5, id);
                    return ps;
                }, keyHolder
        );

        timeEntry.setId(id);

        return timeEntry;
    }

    @Override
    public void delete(Long id) {
        this.template.execute("delete from time_entries where id = " + id );
    }
}

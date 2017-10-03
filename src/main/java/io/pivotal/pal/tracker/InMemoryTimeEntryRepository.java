package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> timeEntryMap = new LinkedHashMap<>();

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(timeEntryMap.size() + 1);
        timeEntryMap.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

    @Override
    public TimeEntry find(Long id) {
        return timeEntryMap.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntryMap.values());
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        TimeEntry found = this.find(id);

        if (found == null) {
            return null;
        }

        timeEntry.setId(id);
        timeEntryMap.put(id, timeEntry);
        return timeEntry;
    }

    @Override
    public void delete(Long id) {
        timeEntryMap.remove(id);
    }
}

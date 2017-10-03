package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    private TimeEntryRepository timeEntryRepository;

    @Autowired
    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry today) {
        TimeEntry timeEntry = timeEntryRepository.create(today);
        return new ResponseEntity(timeEntry, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long l) {

        TimeEntry timeEntry = timeEntryRepository.find(l);
        if (timeEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(timeEntry);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") long l, @RequestBody TimeEntry expected) {

        TimeEntry result = timeEntryRepository.update(l, expected);
        if (result != null) {
            return ResponseEntity.ok(timeEntryRepository.update(l, expected));
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable("id") long l) {
        timeEntryRepository.delete(l);
        return ResponseEntity.noContent().build();
    }
}

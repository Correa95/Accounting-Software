package com.project.backend.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.entity.JournalEntry;
import com.project.backend.service.JournalEntryService;


@RestController
@RequestMapping("companies/{companyId}/journalEntries")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;
    public JournalEntryController(JournalEntryService journalEntryService){
        this.journalEntryService = journalEntryService;
    }

    @GetMapping("/journalEntry")
    public ResponseEntity<List<JournalEntry>> getAllJournalEntries(@PathVariable long companyId) {
        return new ResponseEntity<>(journalEntryService.getAllJournalEntries(companyId), HttpStatus.OK);
    }

    @GetMapping("/{journalEntryId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable long journalEntryId, @PathVariable long companyId) {
        return new ResponseEntity<>(journalEntryService.getJournalEntryById(journalEntryId, companyId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createJournalEntry(@PathVariable long companyId, @RequestBody JournalEntry journalEntry) {
        return new ResponseEntity<>(journalEntryService.createJournalEntry(journalEntry, companyId), HttpStatus.CREATED);
    }

    @PutMapping("/{journalEntryId}")
    public ResponseEntity<JournalEntry> updateJournalEntry(@PathVariable long journalEntryId, @PathVariable long companyId, @RequestBody JournalEntry journalEntry) 
    {
        return new ResponseEntity<>(journalEntryService.updateJournalEntry(journalEntryId, companyId, journalEntry), HttpStatus.OK);
    }

    @DeleteMapping("/{journalEntryId}")
    public void deactivateJournalEntry(@PathVariable long companyId, @PathVariable long journalEntryId){
        journalEntryService.deactivateJournalEntry(journalEntryId, companyId);
        ResponseEntity.noContent().build();
    }

    @PostMapping("/{journalEntryId}/post")
    public ResponseEntity<JournalEntry> postJournalEntry(@PathVariable long companyId, @PathVariable long journalEntryId) {
        return new ResponseEntity<>(journalEntryService.postJournalEntry(journalEntryId, companyId), HttpStatus.OK);
    }

    @PostMapping("/{journalEntryId}/reverse")
    public ResponseEntity<JournalEntry> reverseJournalEntry(@PathVariable long companyId, @PathVariable long journalEntryId,@RequestBody String reason) {
    return new ResponseEntity<>(journalEntryService.reverseJournalEntry(journalEntryId, companyId, reason), HttpStatus.OK);
}
    
}

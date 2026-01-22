package com.project.backend.web;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.project.backend.entity.JournalEntry;
import com.project.backend.repository.JournalEntryRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.backend.service.JournalEntryService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;





@RestController
@RequestMapping("companies/{companyId}/journalEntries")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJournalEntries(@PathVariable long companyId) {
        return new ResponseEntity<>(journalEntryService.getAllJournalEntries(companyId), HttpStatus.OK);
    }

    @GetMapping("/{journalEntryId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable long journalEntryId, @PathVariable long companyId) {
        return new ResponseEntity<>(journalEntryService.getJournalEntryById(journalEntryId, companyId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createJournalEntry(@PathVariable long companyId, @RequestBody JournalEntry journalEntry) {
        return new ResponseEntity<>();
    }

    @PutMapping("/{journalEntryId}")
    public ResponseEntity<JournalEntry> createJournalEntry(@PathVariable long companyId, @RequestBody JournalEntry journalEntry) {
        return new ResponseEntity<>(journalEntryService.createJournalEntry(companyId, JournalEntry), HttpStatus.CREATED);
    }

    // @DeleteMapping("/{journalEntryId}")
    // public void deactivateJournalEntry(@PathVariable long journalEntryId, @PathVariable long journalEntryId){
    //     journalEntryService.deactivateJournalEntry(journalEntryId, journalEntryId);
    //     ResponseEntity.noContent().build();
    // }
    
    
}

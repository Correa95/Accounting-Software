package com.project.backend.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.backend.entity.JournalEntryLine;
import com.project.backend.service.JournalEntryLineService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/companies/{companyId}/journalEntries/{journalEntryId}/journalEntryLine")
@RequiredArgsConstructor
public class JournalEntryLineController {

    private final JournalEntryLineService journalEntryLineService;

    @GetMapping("journalEntryLines")
    public ResponseEntity<List<JournalEntryLine>> getAllJournalEntryLines(
            @PathVariable long companyId,
            @PathVariable long journalEntryId) {
        return new ResponseEntity<>(journalEntryLineService.getAllJournalEntryLines(journalEntryId, companyId), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<JournalEntryLine> addJournalEntryLine(
            @PathVariable long companyId,
            @PathVariable long journalEntryId,
            @RequestBody JournalEntryLine journalEntryLine) {
        return new ResponseEntity<>(journalEntryLineService.addJournalEntryLine(journalEntryId, companyId, journalEntryLine), HttpStatus.CREATED);
    }

    // ---------------- UPDATE LINE ----------------
    @PutMapping("/{journalEntryLineId}")
    public ResponseEntity<JournalEntryLine> updateJournalEntryLine(
            @PathVariable long companyId,
            @PathVariable long journalEntryId, // optional, can ignore inside service
            @PathVariable long journalEntryLineId,
            @RequestBody JournalEntryLine journalEntryLine) {
        return new ResponseEntity<>(journalEntryLineService.updateJournalEntryLine(journalEntryLineId, companyId, journalEntryLine), HttpStatus.OK);
    }

    // ---------------- DELETE LINE ----------------
    @DeleteMapping("/{journalEntryLineId}")
    public ResponseEntity<Void> deleteJournalEntryLine(
            @PathVariable long companyId,
            @PathVariable long journalEntryId, // optional, can ignore inside service
            @PathVariable long journalEntryLineId) {
        journalEntryLineService.deleteJournalEntryLine(journalEntryLineId, companyId);
        return ResponseEntity.noContent().build();
    }
}

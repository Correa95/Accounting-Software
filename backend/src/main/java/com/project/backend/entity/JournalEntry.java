package com.project.backend.entity;

import java.time.LocalDate;
import java.util.List;

import com.project.backend.common.enums.JournalEntryStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "journal_entries", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"company_id", "entryNumber"})
    })
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String entryNumber;

    @Column(nullable = false)
    private LocalDate entryDate;

    @Column(nullable = false)
    private String description;

    @Column(name = "posting_date")
    private LocalDate postingDate;

    @Column(name = "posted_by")
    private String postedBy; // or User entity later

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JournalEntryStatus status;

    @Column(nullable = false)
    private LocalDate createdAt;
    private LocalDate updatedAt;


    @Column(nullable = false)
    private boolean deleted = false;
    private LocalDate deletedAt;



    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

// One-to-many relationship with JournalEntryLine
    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JournalEntryLine> lines;
}

package com.ludwighahn.taskmanager.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String status;  // Can be "pending", "completed", etc.

    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Column(nullable = false)
    private String priority;  // Can be "low", "medium", "high"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Link to the authenticated user

    // Getters and Setters...
}

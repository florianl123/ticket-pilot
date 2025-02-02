package com.mci.ticketpilot.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Project extends AbstractEntity {

    @NotEmpty
    private String projectName;

    @Size(max = 300)
    private String projectDescription;

    private LocalDate projectStartDate;

    private LocalDate projectEndDate;

    // Each Project can have one Manager
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Users projectManager;

    // Each Project can have many Tickets
    @OneToMany(mappedBy = "ticketProject", fetch = FetchType.LAZY)
    private Set<Ticket> projectTickets;


    // Getter and Setter methods
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String title) {
        this.projectName = title;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public Set<Ticket> getTickets() {
        return projectTickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.projectTickets = tickets;
    }

    public Users getManager() {
        return projectManager;
    }

    public void setManager(Users manager) {
        this.projectManager = manager;
    }

    public LocalDate getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(LocalDate projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public LocalDate getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(LocalDate projectEndDate) {
        this.projectEndDate = projectEndDate;
    }
}

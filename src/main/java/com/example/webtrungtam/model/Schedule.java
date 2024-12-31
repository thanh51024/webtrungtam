package com.example.webtrungtam.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "schedule",indexes = {})
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_schedule")
    private int idSchedule;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false,foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ClassEntity idClass;

    @ManyToOne
    @JoinColumn(name = "student_id",columnDefinition = "CHAR(8)", nullable = false,foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Student student;

    @Column(nullable = false)
    private LocalDate date;

}

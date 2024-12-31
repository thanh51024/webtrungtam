package com.example.webtrungtam.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "classes",indexes = {})
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_class")
    private int idClass;

    @ManyToOne
    @JoinColumn(name = "subject_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Subject subject; // Liên kết với môn học

    @ManyToOne
    @JoinColumn(name = "teacher_id",columnDefinition = "CHAR(8)",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @JsonManagedReference(value = "teacher-class")
    private Teacher teacher;

    @Column(name = "class_name", nullable = false)
    private String className; // Tên lớp học

    @Column(name = "number_student")
    private int numberStudent;

    @Column(name = "start_time")
    private Time startTime;

    @Column(name = "end_time")
    private Time endTime;


    @OneToMany(mappedBy = "idClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassStudent> classStudents = new ArrayList<>();

    public void addStudent(Student student) {
        // Tạo đối tượng ClassStudent mới
        ClassStudent classStudent = new ClassStudent(this, student);

        // Thêm vào danh sách của lớp hiện tại
        classStudents.add(classStudent);

        // Đảm bảo student đã có danh sách classStudents
        if (student.getClassStudents() == null) {
            student.setClassStudents(new ArrayList<>()); // Nếu null thì khởi tạo
        }
        student.getClassStudents().add(classStudent); // Thêm vào danh sách của student
    }

    public void removeStudent(Student student) {
        // Tìm đối tượng classStudent trong danh sách hiện tại
        ClassStudent classStudent = null;
        for (ClassStudent cs : classStudents) {
            if (cs.getIdStudent().equals(student)) { // So sánh dựa trên idStudent
                classStudent = cs;
                break;
            }
        }

        if (classStudent != null) { // Nếu tìm thấy, thực hiện xóa
            student.getClassStudents().remove(classStudent); // Xóa khỏi student
            classStudents.remove(classStudent); // Xóa khỏi class
            classStudent.setIdClass(null); // Hủy liên kết
            classStudent.setIdStudent(null); // Hủy liên kết
        }
    }


    public int getIdClass() {
        return idClass;
    }

    public void setIdClass(int idClass) {
        this.idClass = idClass;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getNumberStudent() {
        return numberStudent;
    }

    public void setNumberStudent(int numberStudent) {
        this.numberStudent = numberStudent;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

}

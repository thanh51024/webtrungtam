package com.example.webtrungtam.service.admin.Class;

import com.example.webtrungtam.model.Schedule;
import com.example.webtrungtam.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    // Lấy tất cả lịch học
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    // Tìm lịch học theo ID
    public Optional<Schedule> getScheduleById(int id) {
        return scheduleRepository.findById(id);
    }

    // Thêm hoặc cập nhật lịch học
    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    // Xóa lịch học
    public void deleteSchedule(int id) {
        scheduleRepository.deleteById(id);
    }

    // Tìm lịch học theo lớp
    public List<Schedule> findSchedulesByClass(int idClass) {
        return scheduleRepository.findByIdClass_IdClass(idClass);
    }

    // Tìm lịch học theo học sinh
    public List<Schedule> findSchedulesByStudent(String idStudent) {
        return scheduleRepository.findByStudent_IdStudent(idStudent);
    }
}
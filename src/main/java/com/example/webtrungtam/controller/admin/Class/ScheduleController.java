package com.example.webtrungtam.controller.admin.Class;

import com.example.webtrungtam.model.Schedule;
import com.example.webtrungtam.service.admin.Class.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/schedule-controller")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    // Lấy tất cả lịch học
    @GetMapping("/all")
    public List<Schedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    // Lấy lịch học theo ID
    @GetMapping("/{id}")
    public Optional<Schedule> getScheduleById(@PathVariable int id) {
        return scheduleService.getScheduleById(id);
    }

    // Thêm lịch học
    @PostMapping("/create")
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        return scheduleService.saveSchedule(schedule);
    }

    // Cập nhật lịch học
    @PutMapping("/update/{id}")
    public Schedule updateSchedule(@PathVariable int id, @RequestBody Schedule schedule) {
        schedule.setIdSchedule(id);
        return scheduleService.saveSchedule(schedule);
    }

    // Xóa lịch học
    @DeleteMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable int id) {
        scheduleService.deleteSchedule(id);
        return "Deleted schedule with ID: " + id;
    }

    // Tìm lịch học theo lớp
    @GetMapping("/search_date_class/{idClass}")
    public List<Schedule> findSchedulesByClass(@PathVariable int idClass) {
        return scheduleService.findSchedulesByClass(idClass);
    }
}

package com.test.entity;

public class AttendanceGetleave {

    private String duration_unit;
    private Long duration_percent;
    private Long end_time;
    private Long start_time;
    private String userid;

    public String getDuration_unit() {
        return duration_unit;
    }

    public void setDuration_unit(String duration_unit) {
        this.duration_unit = duration_unit;
    }

    public Long getDuration_percent() {
        return duration_percent;
    }

    public void setDuration_percent(Long duration_percent) {
        this.duration_percent = duration_percent;
    }

    public Long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }

    public Long getStart_time() {
        return start_time;
    }

    public void setStart_time(Long start_time) {
        this.start_time = start_time;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}

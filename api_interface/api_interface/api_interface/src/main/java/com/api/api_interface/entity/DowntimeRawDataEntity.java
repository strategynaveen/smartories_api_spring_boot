package com.api.api_interface.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Entity
@Data
@Table(name = "pdm_downtime_reason_mapping")
public class DowntimeRawDataEntity  implements Serializable
{
    @Id
    @Column(name = "machine_event_id")
    private String machine_event_id;

    @Column(name = "machine_id")
    private String machine_id ;
    @Column(name = "downtime_reason_id", nullable = false)
    private String downtime_reason_id;
    @Column(name = "tool_id", nullable = false)
    private String tool_id;
    @Column(name = "part_id", nullable = false)
    private String part_id;
    @Column(name = "shift_date", nullable = false)
    private String shift_date;
    @Column(name = "start_time", nullable = false)
    private String start_time;
    @Column(name = "end_time", nullable = false)
    private String end_time;
    @Column(name = "split_duration", nullable = false)
    private Float split_duration;
    @Column(name = "calendar_date", nullable = false)
    private String calendar_date;

    public DowntimeRawDataEntity(){

    }

    public DowntimeRawDataEntity(String machine_id,String downtime_reason_id,String tool_id,String part_id,String shift_date,String start_time,String end_time,Float split_duration,String calendar_date){
        this.machine_id=machine_id ;
        this.downtime_reason_id=downtime_reason_id;
        this.tool_id=tool_id;
        this.part_id=part_id;
        this.shift_date=shift_date;
        this.start_time=start_time;
        this.end_time=end_time;
        this.split_duration=split_duration;
        this.calendar_date=calendar_date;
    }

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public String getDowntime_reason_id() {
        return downtime_reason_id;
    }

    public void setDowntime_reason_id(String downtime_reason_id) {
        this.downtime_reason_id = downtime_reason_id;
    }

    public String getTool_id() {
        return tool_id;
    }

    public void setTool_id(String tool_id) {
        this.tool_id = tool_id;
    }

    public String getPart_id() {
        return part_id;
    }

    public void setPart_id(String part_id) {
        this.part_id = part_id;
    }

    public String getShift_date() {
        return shift_date;
    }

    public void setShift_date(String shift_date) {
        this.shift_date = shift_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public Float getSplit_duration() {
        return split_duration;
    }

    public void setSplit_duration(Float split_duration) {
        this.split_duration = split_duration;
    }

    public String getCalendar_date() {
        return calendar_date;
    }

    public void setCalendar_date(String calendar_date) {
        this.calendar_date = calendar_date;
    }

}


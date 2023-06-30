package com.api.api_interface.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "pdm_production_info")
public class ProductionRawDataEntity  implements Serializable
{
    @Id
    // @Column(name = "r_no ")
    private Long r_no;
    @Column(name = "machine_id ")
    private String machine_id ;
    @Column(name = "calendar_date", nullable = false)
    private String calendar_date;
    @Column(name = "shift_date", nullable = false)
    private String shift_date;
    @Column(name = "start_time", nullable = false)
    private String start_time;
    @Column(name = "end_time", nullable = false)
    private String end_time;
    @Column(name = "part_id", nullable = false)
    private String part_id;
    @Column(name = "tool_id", nullable = false)
    private String tool_id;
    @Column(name = "production", nullable = true)
    private Integer production;
    @Column(name = "corrections", nullable = true)
    private Integer corrections;
    @Column(name = "rejections", nullable = true)
    private Integer rejections;
    @Column(name = "reject_reason", nullable = true)
    private String reject_reason;

    public ProductionRawDataEntity(){

    }

    public ProductionRawDataEntity(Long r_no,String machine_id,String calendar_date, String shift_date, String start_time,String end_time,String part_id,String tool_id,Integer production,Integer corrections,Integer rejections,String reject_reason){
        this.r_no = r_no;
        this.machine_id = machine_id;
        this.calendar_date=calendar_date;
        this.shift_date=shift_date;
        this.start_time=start_time;
        this.end_time=end_time;
        this.part_id=part_id;
        this.tool_id=tool_id;
        this.production=production;
        this.corrections=corrections;
        this.rejections=rejections;
        this.reject_reason=reject_reason;
    }

    public Long getR_no() {
        return r_no;
    }

    public void setR_no(Long r_no) {
        this.r_no = r_no;
    }

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public String getCalendar_date() {
        return calendar_date;
    }

    public void setCalendar_date(String calendar_date) {
        this.calendar_date = calendar_date;
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

    public String getPart_id() {
        return part_id;
    }

    public void setPart_id(String part_id) {
        this.part_id = part_id;
    }

    public String getTool_id() {
        return tool_id;
    }

    public void setTool_id(String tool_id) {
        this.tool_id = tool_id;
    }

    public Integer getProduction() {
        return production;
    }

    public void setProduction(Integer production) {
        this.production = production;
    }

    public Integer getCorrections() {
        return corrections;
    }

    public void setCorrections(Integer corrections) {
        this.corrections = corrections;
    }

    public Integer getRejections() {
        return rejections;
    }

    public void setRejections(Integer rejections) {
        this.rejections = rejections;
    }

    public String getReject_reason() {
        return reject_reason;
    }

    public void setReject_reason(String reject_reason) {
        this.reject_reason = reject_reason;
    }
}


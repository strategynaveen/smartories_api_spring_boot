package com.api.api_interface.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "settings_financial_metrics_goals")
public class OverallTargetValuesEntity  implements Serializable
//public class User
{
    public Float getOverall_teep() {
        return overall_teep;
    }

    public void setOverall_teep(Float overall_teep) {
        this.overall_teep = overall_teep;
    }

    public Float getOverall_ooe() {
        return overall_ooe;
    }

    public void setOverall_ooe(Float overall_ooe) {
        this.overall_ooe = overall_ooe;
    }

    public Float getOverall_oee() {
        return overall_oee;
    }

    public void setOverall_oee(Float overall_oee) {
        this.overall_oee = overall_oee;
    }

    public Float getAvailability() {
        return availability;
    }

    public void setAvailability(Float availability) {
        this.availability = availability;
    }

    public Float getPerformance() {
        return performance;
    }

    public void setPerformance(Float performance) {
        this.performance = performance;
    }

    public Float getQuality() {
        return quality;
    }

    public void setQuality(Float quality) {
        this.quality = quality;
    }

    public Float getOee_target() {
        return oee_target;
    }

    public void setOee_target(Float oee_target) {
        this.oee_target = oee_target;
    }

    @Id
    @Column(name = "overall_teep")
    private Float overall_teep ;
    @Column(name = "overall_ooe")
    private Float overall_ooe;
    @Column(name = "overall_oee")
    private Float overall_oee;
    @Column(name = "availability")
    private Float availability;
    @Column(name = "performance")
    private Float performance;
    @Column(name = "quality")
    private Float quality;
    @Column(name = "oee_target")
    private Float oee_target;

    public OverallTargetValuesEntity(){

    }

    public OverallTargetValuesEntity(Float overall_teep,Float overall_ooe,Float overall_oee,Float availability,Float performance,Float quality,Float oee_target){
        this.overall_teep = overall_teep;
        this.overall_ooe=overall_ooe;
        this.overall_oee=overall_oee;
        this.availability=availability;
        this.performance=performance;
        this.quality=quality;
        this.oee_target=oee_target;
    }

}

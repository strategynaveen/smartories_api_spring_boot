package com.api.api_interface.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "settings_quality_reasons")
public class Quality_ReasonEntity implements Serializable  {

    @Id
    private String quality_reason_id;

    private String quality_reason_name;

    private String image_id;

    private String last_updated_on;

    private String last_updated_by;

    private  String status;


    public Quality_ReasonEntity() {
    }

    public Quality_ReasonEntity(String quality_reason_id, String quality_reason_name, String image_id, String last_updated_on, String last_updated_by, String status) {
        this.quality_reason_id = quality_reason_id;
        this.quality_reason_name = quality_reason_name;
        this.image_id = image_id;
        this.last_updated_on = last_updated_on;
        this.last_updated_by = last_updated_by;
        this.status = status;
    }

    public String getQuality_reason_id() {
        return quality_reason_id;
    }

    public void setQuality_reason_id(String quality_reason_id) {
        this.quality_reason_id = quality_reason_id;
    }

    public String getQuality_reason_name() {
        return quality_reason_name;
    }

    public void setQuality_reason_name(String quality_reason_name) {
        this.quality_reason_name = quality_reason_name;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getLast_updated_on() {
        return last_updated_on;
    }

    public void setLast_updated_on(String last_updated_on) {
        this.last_updated_on = last_updated_on;
    }

    public String getLast_updated_by() {
        return last_updated_by;
    }

    public void setLast_updated_by(String last_updated_by) {
        this.last_updated_by = last_updated_by;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

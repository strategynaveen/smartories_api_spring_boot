package com.api.api_interface.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "settings_machine_log")
public class InactiveMachineValuesEntity  implements Serializable
//public class User
{
    @Id
    @Column(name = "machine_id")
    private String machine_id ;
    @Column(name = "status", nullable = false)
    private Integer status;
    @Column(name = "last_updated_on")
    private String last_updated_on;

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLast_updated_on() {
        return last_updated_on;
    }

    public void setLast_updated_on(String last_updated_on) {
        this.last_updated_on = last_updated_on;
    }

    public InactiveMachineValuesEntity(){

    }

    public InactiveMachineValuesEntity(String machine_id,Integer status, String last_updated_on){
        this.machine_id = machine_id;
        this.status=status;
        this.last_updated_on=last_updated_on;
    }

}

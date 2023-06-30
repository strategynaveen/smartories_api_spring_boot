package com.api.api_interface.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "settings_machine_current")
public class MachineValuesEntity  implements Serializable
//public class User
{
    @Id
    @Column(name = "machine_id ")
    private String machine_id ;
    @Column(name = "machine_name", nullable = false)
    private String machine_name;
    @Column(name = "rate_per_hour", nullable = false)
    private Float rate_per_hour;
    @Column(name = "machine_offrate_per_hour", nullable = false)
    private Float machine_offrate_per_hour;
    @Column(name = "status")
    private Integer status;

    public MachineValuesEntity(){

    }

    public MachineValuesEntity(String machine_id,String machine_name, Float rate_per_hour, Float machine_offrate_per_hour){
        this.machine_id = machine_id;
        this.machine_name=machine_name;
        this.rate_per_hour=rate_per_hour;
        this.machine_offrate_per_hour=machine_offrate_per_hour;
    }
    public String getMachine_id() {
        return machine_id;
    }
    public void setMachine_id(String  machine_id) {
        this.machine_id = machine_id;
    }
    public String getMachine_name(){
        return  machine_name;
    }
    public void  setMachine_name(String machine_name){
        this.machine_name=machine_name;
    }
    public Float getRate_per_hour(){
        return  rate_per_hour;
    }
    public void  setRate_per_hour(Float rate_per_hour){
        this.rate_per_hour=rate_per_hour;
    }
    public Float getMachine_offrate_per_hour(){
        return  machine_offrate_per_hour;
    }
    public void  setMachine_offrate_per_hour(Float machine_offrate_per_hour){
        this.machine_offrate_per_hour=machine_offrate_per_hour;
    }
}

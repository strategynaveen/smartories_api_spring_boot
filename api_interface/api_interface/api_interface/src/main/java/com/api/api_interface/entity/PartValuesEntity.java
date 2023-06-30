package com.api.api_interface.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "settings_part_current")
public class PartValuesEntity  implements Serializable
//public class User
{
    @Id
    @Column(name = "part_id")
    private String part_id ;
    @Column(name = "part_name", nullable = false)
    private String part_name;
    @Column(name = "NICT", nullable = false)
    private Integer nict;
    @Column(name = "tool_id", nullable = false)
    private String tool_id;
    @Column(name = "part_price", nullable = false)
    private Float part_price;

    public PartValuesEntity(){

    }

    public String getPart_id() {
        return part_id;
    }

    public void setPart_id(String part_id) {
        this.part_id = part_id;
    }

    public String getPart_name() {
        return part_name;
    }

    public void setPart_name(String part_name) {
        this.part_name = part_name;
    }

    public Integer getNict() {
        return nict;
    }

    public void setNict(Integer nict) {
        this.nict = nict;
    }

    public String getTool_id() {
        return tool_id;
    }

    public void setTool_id(String tool_id) {
        this.tool_id = tool_id;
    }

    public Float getPart_price() {
        return part_price;
    }

    public void setPart_price(Float part_price) {
        this.part_price = part_price;
    }

    public PartValuesEntity(String part_id, String part_name, Integer nict, String tool_id, Float part_price){
        this.part_id = part_id;
        this.part_name=part_name;
        this.nict=nict;
        this.tool_id=tool_id;
        this.part_price=part_price;
    }

}

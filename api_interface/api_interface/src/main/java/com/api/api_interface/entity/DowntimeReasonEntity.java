package com.api.api_interface.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Entity
@Data
@Table(name = "settings_downtime_reasons")
public class DowntimeReasonEntity  implements Serializable
{
    @Id
    @Column(name = "downtime_reason_id")
    private String downtime_reason_id;
    @Column(name = "downtime_category")
    private String downtime_category ;
    @Column(name = "downtime_reason")
    private String downtime_reason;

}
package com.tickets.entity;

import lombok.Data;

import java.util.Date;

/**
 * 活动管理，对应数据表activity
 */
@Data
public class VenueActivies {

    private String aaId;
    /**
     * 主键
     */
    private String aId;
    /**
     * 活动名称
     */
    private String aName;
    /**
     * 活动场馆
     */
    private String vName;
    /**
     * 状态，1不启用，2启动
     */
    private Integer aEnable;
    /**
     * 备注
     */
    private String aNote;
    /**
     * 创建时间
     */
    private Date aCreationtime;
    /**
     * 票务数量
     */
    private Integer aTicketnumber;
    /**
     * 工作证总数
     */
    private Integer aEmployeenumber;
    /**
     * 海报图片
     */
    private String aImage;
    /**
     * 活动类型
     */
    private String aType;
    /**
     * 活动开始时间
     */
    private Date startTime;
    /**
     * 活动结束时间
     */
    private Date endTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}

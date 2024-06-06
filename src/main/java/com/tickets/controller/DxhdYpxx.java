package com.tickets.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 验票信息 实体类。
 *
 * @author account
 * @since 2023-09-06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DxhdYpxx implements Serializable {

    /**
     * 数据ID
     */

    private String id;

    /**
     * 场次ID（幂等）
     */

    private String performId;

    /**
     * 项目ID（幂等）
     */

    private String projectId;

    /**
     * 验票通道名称
     */
    private String checkPass;

    /**
     * 座位行号（无座项目,可能无此信息）
     */

    private String seatRow;

    /**
     * 座位列号（无座项目,可能无此信息）
     */
    private String seatCol;

    /**
     * 验票方式的类型
     */
    private String checkCertType;

    /**
     * 根据checkCertType判断，如type是身份证件类型，则该字段为证件号。如type是非身份证件类型，则该字段仅为凭证号。支付宝电子身份证为电子串码，所以证件号字段大小请勿进行限制。
     */
    private String checkCertTypeNo;

    /**
     * 场馆名称
     */
    private String venueName;

    /**
     * 入场时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date entryTime;

    /**
     * 验票时间（必填）
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;

    /**
     * 票单ID（必填）（幂等）
     */
    private String voucherId;

    /**
     * 入场验票证件号
     */
    private String bindCard;

    /**
     * 入场人姓名
     */
    private String bindUserName;

    /**
     * 楼层（必填）（无座项目,可能无此信息）
     */
    private String floorName;

    /**
     * 区域（必填）（无座项目,可能无此信息）
     */
    private String standName;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备号
     */
    private String deviceCode;

    /**
     * 入库时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date insertTime;

    /**
     * 人脸图片（base64）
     */
    private String bindPhoto;

    /**
     * 证件图片（base64）
     */
    private String bindCardPhoto;

    /**
     * 身份证号码
     */
    private String idcode;

    /**
     * 户籍地信息
     */
    private String hjdxx;

    /**
     * 身份证件民族信息
     */
    private String mzxx;
}

package com.tickets.dto;

import java.text.SimpleDateFormat;

public class TloeAddDto {

    private String teId;
    private String teAisle;
    private String teaId;
    private String teCategory;
    private String teMarking;
    private String teDate;
    private byte[] teImage;

    public TloeAddDto() {
    }

    public TloeAddDto(String teId, String teAisle, String teaId, String teCategory, String teMarking, String teDate, byte[] teImage) {
        this.teId = teId;
        this.teAisle = teAisle;
        this.teaId = teaId;
        this.teCategory = teCategory;
        this.teMarking = teMarking;
        this.teDate = teDate;
        this.teImage = teImage;
    }

    public void setTeId(String teId) {
        this.teId = teId;
    }

    public void setTeAisle(String teAisle) {
        this.teAisle = teAisle;
    }

    public void setTeaId(String teaId) {
        this.teaId = teaId;
    }

    public void setTeCategory(String teCategory) {
        this.teCategory = teCategory;
    }

    public void setTeMarking(String teMarking) {
        this.teMarking = teMarking;
    }

    public void setTeDate(String teDate) {
        this.teDate = teDate;
    }

    public void setTeImage(byte[] teImage) {
        this.teImage = teImage;
    }

    public String getTeId() {
        return teId;
    }

    public String getTeAisle() {
        return teAisle;
    }

    public String getTeaId() {
        return teaId;
    }

    public String getTeCategory() {
        return teCategory;
    }

    public String getTeMarking() {
        return teMarking;
    }

    public String getTeDate() {
        return teDate;
    }

    public byte[] getTeImage() {
        return teImage;
    }
}

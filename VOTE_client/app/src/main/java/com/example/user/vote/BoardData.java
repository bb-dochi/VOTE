package com.example.user.vote;

import java.io.Serializable;

public class BoardData implements Serializable {
    private int index;
    private String category;
    private String id;
    private String title;
    private String memo;
    private String list1;
    private String list2;
    private String img1;
    private String img2;
    private int vote1;
    private int vote2;
    private int noname;
    private String alarm;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId(){
	return id;
    }

    public void setId(String id){
	this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getList1() {
        return list1;
    }

    public void setList1(String list1) {
        this.list1 = list1;
    }

    public String getList2() {
        return list2;
    }

    public void setList2(String list2) {
        this.list2 = list2;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public int getVote1() {
        return vote1;
    }

    public void setVote1(int vote1) {
        this.vote1 = vote1;
    }

    public int getVote2() {
        return vote2;
    }

    public void setVote2(int vote2) {
        this.vote2 = vote2;
    }

    public int getNoname() {
        return noname;
    }

    public void setNoname(int noname) {
        this.noname = noname;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }
}


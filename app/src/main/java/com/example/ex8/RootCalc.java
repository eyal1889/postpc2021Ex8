package com.example.ex8;

import java.util.UUID;

import static java.lang.Math.sqrt;

public class RootCalc {
    private boolean is_done = false;
    private String answer = "";
    private Long number;
    private long sqrt_num=0;
    UUID id ;
    private int progress;
    public RootCalc(Long number, UUID id){
        this.number = number;
        this.id = id;
        this.progress=0;
        this.sqrt_num = (long)sqrt(this.number);
    }
    public boolean get_status(){ return this.is_done; }
    public void set_status(boolean new_status){ this.is_done = new_status; }
    public Long get_number(){ return this.number; }
    public void set_answer(String answer){this.answer = answer; }
    public String get_nswer(){return this.answer; }

    public void setProgress(int progress) {
        this.progress = progress;
    }
    public int getProgress(){
        return this.progress;
    }

    public long getSqrt_num() {
        return sqrt_num;
    }

    public void setSqrt_num(long sqrt_num) {
        this.sqrt_num = sqrt_num;
    }
}

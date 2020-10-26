package com.treCoops.watchg1.Model;

public class Watch {

    int alert;
    int sound;
    int trigger;
    int vibration;
    int waitingTime;

    public Watch(){}

    public Watch(int alert, int sound, int trigger, int vibration, int waitingTime){
        this.alert = alert;
        this.sound = sound;
        this.trigger = trigger;
        this.vibration = vibration;
        this.waitingTime = waitingTime;
    }

    public int getAlert() {
        return alert;
    }

    public int getSound() {
        return sound;
    }

    public int getTrigger() {
        return trigger;
    }

    public int getVibration() {
        return vibration;
    }

    public int getWaitingTime() {
        return waitingTime;
    }
}

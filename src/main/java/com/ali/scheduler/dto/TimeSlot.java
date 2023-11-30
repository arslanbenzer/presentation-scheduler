package com.ali.scheduler.dto;

import java.util.ArrayList;
import java.util.List;

public class TimeSlot {
    private Integer remainingCapacity;
    private List<Presentation> presentations;

    public TimeSlot(Integer capacity) {
        this.remainingCapacity = capacity;
        presentations = new ArrayList<>();
    }

    public Integer getRemainingCapacity() {
        return remainingCapacity;
    }

    public void setRemainingCapacity(Integer remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }

    public List<Presentation> getPresentations() {
        return presentations;
    }

    public void addPresentation(Presentation p) {
        this.presentations.add(p);
        setRemainingCapacity(remainingCapacity - p.getDuration());
    }

    public void removePresentation(Presentation p) {
        this.presentations.remove(p);
    }
}

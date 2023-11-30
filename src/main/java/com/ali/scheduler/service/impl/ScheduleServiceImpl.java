package com.ali.scheduler.service.impl;

import com.ali.scheduler.dto.Presentation;
import com.ali.scheduler.dto.TimeSlot;
import com.ali.scheduler.service.ScheduleService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Override
    public String schedulePresentations(String data) {
        String[] presentations = data.split("\r\n");
        List<Presentation> presentationsList = new ArrayList<>();

        List<TimeSlot> timeSlots = new ArrayList<>();
        timeSlots.add(new TimeSlot(3 * 60));
        timeSlots.add(new TimeSlot(4 * 60));

        parsePresentations(presentations, presentationsList);

        presentationsList.sort(Comparator.comparing(Presentation::getDuration).reversed());

        schedule(presentationsList, timeSlots);

        StringBuilder sb = printOutput(timeSlots);

        return sb.toString().trim();
    }

    private static StringBuilder printOutput(List<TimeSlot> timeSlots) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (TimeSlot t : timeSlots) {
            Calendar c = Calendar.getInstance();
            if (index++ % 2 == 1) {
                sb.append("Track " + ((index + 1) / 2) + ":\n");
                c.set(Calendar.HOUR_OF_DAY, 9);
                c.set(Calendar.MINUTE, 0);
            } else {
                c.set(Calendar.HOUR_OF_DAY, 12);
                c.set(Calendar.MINUTE, 0);
                sb.append(sdf.format(c.getTime()));
                sb.append(" Lunch\n");
                c.set(Calendar.HOUR_OF_DAY, 13);
            }

            for (Presentation p : t.getPresentations()) {
                sb.append(sdf.format(c.getTime()));
                sb.append(" ");
                sb.append(p.getName());
                sb.append(" ");
                sb.append(p.getDuration() == 5 ? "lightning" : p.getDuration() + "min");
                sb.append("\n");
                c.add(Calendar.MINUTE, p.getDuration());
            }
            if (index % 2 == 1) {
                if (c.get(Calendar.HOUR_OF_DAY) < 16) {
                    c.set(Calendar.HOUR_OF_DAY, 16);
                }
                if (c.get(Calendar.HOUR_OF_DAY) == 17) {
                    sb.append("\n");
                    continue;
                }
                sb.append(sdf.format(c.getTime()));
                sb.append(" ");
                sb.append("Networking Event\n");
                sb.append("\n");
            }
        }
        return sb;
    }

    private void schedule(List<Presentation> presentationsList, List<TimeSlot> timeSlots) {
        while (!presentationsList.isEmpty()) {
            Presentation current = presentationsList.get(0);
            boolean isAdded = false;
            for (TimeSlot timeSlot : timeSlots) {
                if (timeSlot.getRemainingCapacity() >= current.getDuration()) {
                    timeSlot.addPresentation(current);
                    presentationsList.remove(current);
                    isAdded = true;
                    break;
                }
            }
            if (!isAdded) {
                timeSlots.add(new TimeSlot(3 * 60));
                timeSlots.add(new TimeSlot(4 * 60));
            }
        }
    }

    private void parsePresentations(String[] presentations, List<Presentation> presentationsList) {
        for (String presentation : presentations) {
            String[] tokens = presentation.split(" ");
            String name = String.join(" ", Arrays.copyOfRange(tokens, 0, tokens.length - 1));
            String durationStr = tokens[tokens.length - 1];
            int duration;
            if ("lightning".equals(durationStr)) {  // 5min presentations
                duration = 5;
            } else {
                duration = Integer.parseInt(durationStr.replace("min", ""));
            }
            presentationsList.add(new Presentation(name, duration));
        }
    }
}

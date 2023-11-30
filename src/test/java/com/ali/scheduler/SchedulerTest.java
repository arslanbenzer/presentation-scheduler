package com.ali.scheduler;

import com.ali.scheduler.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SchedulerTest {

    @Autowired
    private ScheduleService scheduleService;

    @Test
    void scheduleShouldAssignToTimeSlots() {
        String testData = """
                Architecting Your Codebase 60min\r
                Overdoing it in Python 45min\r
                Flavors of Concurrency in Java 30min\r
                Ruby Errors from Mismatched Gem Versions 45min\r
                JUnit 5 - Shaping the Future of Testing on the JVM 45min\r
                Cloud Native Java lightning\r
                Communicating Over Distance 60min\r
                AWS Technical Essentials 45min\r
                Continuous Delivery 30min\r
                Monitoring Reactive Applications 30min\r
                Pair Programming vs Noise 45min\r
                Rails Magic 60min\r
                Microservices "Just Right" 60min\r
                Clojure Ate Scala (on my project) 45min\r
                Perfect Scalability 30min\r
                Apache Spark 30min\r
                Async Testing on JVM 60min\r
                A World Without HackerNews 30min\r
                User Interface CSS in Apps 30min""";

        String expectedResult = """
                Track 1:
                09:00 AM Architecting Your Codebase 60min
                10:00 AM Communicating Over Distance 60min
                11:00 AM Rails Magic 60min
                12:00 PM Lunch
                01:00 PM Microservices "Just Right" 60min
                02:00 PM Async Testing on JVM 60min
                03:00 PM Overdoing it in Python 45min
                03:45 PM Ruby Errors from Mismatched Gem Versions 45min
                04:30 PM Flavors of Concurrency in Java 30min

                Track 2:
                09:00 AM JUnit 5 - Shaping the Future of Testing on the JVM 45min
                09:45 AM AWS Technical Essentials 45min
                10:30 AM Pair Programming vs Noise 45min
                11:15 AM Clojure Ate Scala (on my project) 45min
                12:00 PM Lunch
                01:00 PM Continuous Delivery 30min
                01:30 PM Monitoring Reactive Applications 30min
                02:00 PM Perfect Scalability 30min
                02:30 PM Apache Spark 30min
                03:00 PM A World Without HackerNews 30min
                03:30 PM User Interface CSS in Apps 30min
                04:00 PM Cloud Native Java lightning
                04:05 PM Networking Event""";

        String result = scheduleService.schedulePresentations(testData);
        assertEquals(expectedResult, result);
    }

    @Test
    void scheduleAllLightningPresentations(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 7 * 12 * 8; i++){
            sb.append("Test presentation " + i +  " lightning\r\n");
        }
        String testData = sb.toString();

        String result = scheduleService.schedulePresentations(testData);
        assertTrue(result.contains("Track 8"));
        assertTrue(result.endsWith("04:55 PM Test presentation 671 lightning"));
    }


    @Test
    void scheduleLongPresentations(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 12; i++){
            sb.append("Test presentation " + i +  " 75min\r\n");
        }
        for(int i = 0; i < 6; i++){
            sb.append("Test presentation " + i +  " 30min\r\n");
        }
        for(int i = 0; i < 6; i++){
            sb.append("Test presentation " + i +  " 45min\r\n");
        }
        String testData = sb.toString();

        String result = scheduleService.schedulePresentations(testData);
        assertTrue(!result.contains("Track 5"));
        assertTrue(result.endsWith("04:00 PM Networking Event"));
    }
}

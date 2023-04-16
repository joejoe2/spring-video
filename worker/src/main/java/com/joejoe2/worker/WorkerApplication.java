package com.joejoe2.worker;

import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkerApplication {
  private static final CountDownLatch latch = new CountDownLatch(1);

  public static void main(String[] args) {
    Locale.setDefault(Locale.ENGLISH);
    SpringApplication.run(WorkerApplication.class, args);
    Runtime.getRuntime().addShutdownHook(new Thread(() -> latch.countDown()));
    try {
      latch.await();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }
}

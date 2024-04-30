package com.studentdb.jdbcspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.studentdb.jdbcspring.server.serverChannel;




@SpringBootApplication(scanBasePackages = "com.studentdb/src/main")
@ComponentScan("com.studentdb.jdbcspring.server")
public class Application {
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class, args);
		System.out.println("Running.....");
		serverChannel sc = new serverChannel();
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                	try {
						sc.startListening();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    System.out.println("Listening Thread is running");
                    try {
                        Thread.sleep(5000); // Sleep for 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start(); // Start the thread
	}
	}
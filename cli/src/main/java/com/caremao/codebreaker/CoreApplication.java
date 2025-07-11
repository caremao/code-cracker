package com.caremao.codebreaker;

import com.caremao.codebreaker.runner.CliMainRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import picocli.CommandLine;

@SpringBootApplication
public class CoreApplication implements CommandLineRunner {

    private final ApplicationContext context;

    public CoreApplication(ApplicationContext context) {
        this.context = context;
    }

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

    @Override
    public void run(String... args) {
//        new CommandLine(context.getBean(CliLanternaRunner.class)).execute(args);
        new CommandLine(context.getBean(CliMainRunner.class)).execute(args);
    }

}

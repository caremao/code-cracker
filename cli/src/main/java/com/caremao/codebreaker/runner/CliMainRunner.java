package com.caremao.codebreaker.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
//import picocli.CommandLine;CommandLine
//import picocli.CommandLine.Command;
//import picocli.CommandLine.Option;
//import picocli.CommandLine.Parameters;

@Component
public class CliMainRunner implements ApplicationRunner, Runnable
{


    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Get the list

        // Confirm the list

        // Filter with response (not the first time)
        // Process list
        // 	Create models
        //
        // Order
        // Select candidate
        // Input selection and response
    }

    @Override
    public void run() {
        // TODO document why this method is empty
    }
}

package org.niitp.camundalongpolling;

import org.springframework.boot.SpringApplication;

public class TestCamundaLongPollingApplication {

    public static void main(String[] args) {
        SpringApplication.from(CamundaLongPollingApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}

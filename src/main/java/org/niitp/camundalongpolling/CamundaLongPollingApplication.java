package org.niitp.camundalongpolling;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;
import java.util.Random;

@Deployment(resources = {"classpath*:calc-insurance-index.bpmn"})
@SpringBootApplication
public class CamundaLongPollingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CamundaLongPollingApplication.class, args);
    }

}

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
class StartFormRestController {

    private final ZeebeClient zeebe;

    @PostMapping("/start")
    public void startProcessInstance(@RequestBody Map<String, Object> variables) {

        log.info("Starting process `process-insurance-index` with variables: {}", variables);

        zeebe
                .newCreateInstanceCommand()
                .bpmnProcessId("process-insurance-index")
                .latestVersion()
                .variables(variables)
                .send();
    }
}


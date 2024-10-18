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

@Component
@Slf4j
@SuppressWarnings("unused")
@RequiredArgsConstructor
class InsuranceIndex {

    @JobWorker(type = "calc-insurance-index", timeout = 1800000)
    public Map<String, Object> handle(JobClient client, ActivatedJob job) throws InterruptedException {
        log.info("Запуск jobWorker: {}", job.getType());

        // Получаем переменные задачи
        Map<String, Object> variables = job.getVariablesAsMap();

        // Имитация расчета индексов
        int insuranceIndex1 = generateRandomIndex();
        int insuranceIndex2 = generateRandomIndex();

        // Пауза на 30 секунд для имитации длительного расчета
        Thread.sleep(Duration.ofSeconds(30).toMillis());

        // Возвращаем результаты расчетов в виде Map
        return Map.of("insuranceIndex1", insuranceIndex1, "insuranceIndex2", insuranceIndex2);
    }

    // Генерация случайного индекса
    private int generateRandomIndex() {
        Random random = new Random();
        return random.nextInt(100); // Генерация целого числа от 0 до 99
    }
}


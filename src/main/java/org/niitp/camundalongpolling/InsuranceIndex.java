package org.niitp.camundalongpolling;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.Random;

@Component
@Slf4j
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class InsuranceIndex {

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

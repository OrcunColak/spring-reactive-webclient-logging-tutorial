package com.colak.springtutorial.quote.client.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoggingQuoteClient {

    private final WebClient logginWebclient;

    public LoggingQuoteClient(@Qualifier("loggingWebclient") WebClient logginWebclient) {
        this.logginWebclient = logginWebclient;
    }

    public List<String> getQuotesByLogging() {
        Flux<String> flux = getQuotesByWebFlux();
        return flux.collectList().block();
    }

    private Flux<String> getQuotesByWebFlux() {
        List<Flux<String>> list = new ArrayList<>();

        for (int index = 0; index < 3; index++) {
            Flux<String> flux = logginWebclient
                    .get()
                    .uri("http://localhost:8080/api/v1/quote/getQuotes")
                    .retrieve()
                    .bodyToFlux(String.class);
            list.add(flux);
        }
        return Flux.merge(list);
    }
}

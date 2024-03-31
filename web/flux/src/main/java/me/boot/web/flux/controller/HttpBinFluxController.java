package me.boot.web.flux.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Duration;
import java.time.LocalTime;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.boot.httputil.service.HttpBinService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

/**
 * HttpBinController
 *
 * @since 2024/03/09
 **/
@Tag(name = "HttpBin", description = "WebFlux")
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/bin")
public class HttpBinFluxController {

    private final HttpBinService httpBinService;

    @Operation(summary = "Generate UUID")
    @ApiResponse(responseCode = "200", description = "UUID")
    @GetMapping("uuid")
    public Mono<String> uuid() {
        return Mono.just(httpBinService.uuid().get("uuid")).onErrorReturn("ERROR");
    }

    @Operation(summary = "Flux - Server Sent Events")
    @GetMapping(value = "/sse/{count}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> flux(@PathVariable @Range(min = 2, max = 100) int count) {
        return Flux
            .zip(Flux.fromStream(IntStream.range(1, count).mapToObj(i -> "flux data--" + i)),
                Flux.interval(Duration.ofSeconds(1)))
            .map(Tuple2::getT1);
    }

    @GetMapping("/stream-sse")
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofSeconds(1))
            .map(sequence -> ServerSentEvent.<String>builder()
                .id(String.valueOf(sequence))
                .event("periodic-event")
                .data("SSE - " + LocalTime.now())
                .build());
    }

}

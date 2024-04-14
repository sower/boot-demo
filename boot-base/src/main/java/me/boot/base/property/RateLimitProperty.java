package me.boot.base.property;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RateLimitProperty
 *
 * @since 2024/04/11
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateLimitProperty {

    String key;

    long qps;

    Duration timeout;

}

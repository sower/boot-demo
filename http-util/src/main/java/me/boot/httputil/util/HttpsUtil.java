package me.boot.httputil.util;

import lombok.SneakyThrows;
import org.springframework.http.HttpMethod;

public interface HttpsUtil {

  @SneakyThrows
  String executeRequest(String method);

   default String get() {
    return executeRequest(HttpMethod.GET.name());
  }

   default String post() {
    return executeRequest(HttpMethod.POST.name());
  }

   default String put() {
    return executeRequest(HttpMethod.PUT.name());
  }

   default String delete() {
    return executeRequest(HttpMethod.DELETE.name());
  }
}

package me.boot.httputil.config;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * https客户端
 * @date 2023/03/26
 **/
@Slf4j
@Configuration
public class HttpsClient {

  @Bean
  public CloseableHttpClient closeableHttpClient(HttpsClientConfig config) {
    //  创建请求配置信息
    RequestConfig requestConfig = RequestConfig.custom()
        // 设置连接超时时间
        .setConnectionRequestTimeout(Timeout.ofSeconds(config.connectionTimeout))
        // 设置响应超时时间
        .setResponseTimeout(config.readTimeout, TimeUnit.SECONDS)
        // 设置从连接池获取链接的超时时间
        .setConnectionRequestTimeout(config.callTimeout, TimeUnit.SECONDS)
        .setConnectionKeepAlive(TimeValue.ofSeconds(config.keepAliveTimeout))
        .build();

    ConnectionConfig connConfig = ConnectionConfig.custom()
        .setConnectTimeout(config.connectionTimeout, TimeUnit.SECONDS)
        .setSocketTimeout(config.connectionTimeout, TimeUnit.SECONDS)
        .build();

    BasicHttpClientConnectionManager cm = connectionManager();
    cm.setConnectionConfig(connConfig);

    return HttpClients.custom()
        .setDefaultHeaders(Collections.emptyList())
        .setDefaultRequestConfig(requestConfig)
        .setConnectionManager(cm)
        .build();
  }

  /**
   * HC SSL connectionManager
   */
  @SneakyThrows
  public BasicHttpClientConnectionManager connectionManager() {
    TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
    SSLContext sslContext = SSLContexts.custom()
        .loadTrustMaterial(null, acceptingTrustStrategy)
        .build();
    SSLConnectionSocketFactory sslsf =
        new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
    Registry<ConnectionSocketFactory> socketFactoryRegistry =
        RegistryBuilder.<ConnectionSocketFactory>create()
            .register("https", sslsf)
            .register("http", new PlainConnectionSocketFactory())
            .build();

    return new BasicHttpClientConnectionManager(socketFactoryRegistry);
  }

  @Bean
  public OkHttpClient okHttpClient(HttpsClientConfig config) {
    TrustManager[] trustManagers = buildTrustManagers();
    return
        new OkHttpClient()
            .newBuilder()
            .addInterceptor(new BasicLoggingInterceptor())
            //      .cache(cache) // configure cache
            //      .proxy(proxy) // configure proxy
            //      .certificatePinner(certificatePinner) // certificate pinning
            //      .addNetworkInterceptor(interceptor) // network level interceptor
            //      .authenticator(authenticator) // authenticator for requests (it supports similar
            // use-cases as "Authorization header" earlier
            .callTimeout(
                config.getCallTimeout(),
                TimeUnit.SECONDS) // default timeout for complete calls
            .readTimeout(
                config.getReadTimeout(),
                TimeUnit.SECONDS) // default read timeout for new connections
            .writeTimeout(
                config.getWriteTimeout(),
                TimeUnit.SECONDS) // default write timeout for new connections
            //      .dns(dns) // DNS service used to lookup IP addresses for hostnames
            //      .followRedirects(true) // follow requests redirects
            //      .followSslRedirects(true) // follow HTTP tp HTTPS redirects
            //      .connectionPool(connectionPool) // connection pool used to recycle HTTP and
            // HTTPS connections
            //      .retryOnConnectionFailure(true) // retry or not when a connectivity problem is
            // encountered
            //      .cookieJar(cookieJar) // cookie manager
            //      .dispatcher(dispatcher) // dispatcher used to set policy and execute
            // asynchronous requests
            .sslSocketFactory(
                createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0])
            .hostnameVerifier((hostName, session) -> StringUtils.isNotBlank(hostName))
            // 设置连接池  最大连接数量  , 持续存活的连接
            .connectionPool(
                new ConnectionPool(
                    config.getConnectionTimeout(),
                    config.getKeepAliveTimeout(),
                    TimeUnit.MINUTES))
            .build();
  }


  /**
   * 生成安全套接字工厂，用于https请求的证书跳过
   */
  public static SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
    SSLSocketFactory ssfFactory = null;
    try {
      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, trustAllCerts, new SecureRandom());
      ssfFactory = sc.getSocketFactory();
    } catch (Exception e) {
      log.info("Create SSLSocketFactory error", e);
    }
    return ssfFactory;
  }

  public static TrustManager[] buildTrustManagers() {
    return new TrustManager[]{
        new X509TrustManager() {
          @Override
          public void checkClientTrusted(X509Certificate[] chain, String authType) {
          }

          @Override
          public void checkServerTrusted(X509Certificate[] chain, String authType) {
          }

          @Override
          public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
          }
        }
    };
  }

  public static class BasicLoggingInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
      Request request = chain.request();
      log.info("===> Sending {} request: {}", request.method(), request.url());

      log.info("request headers: {}", request.headers());

      RequestBody requestBody = request.body();
      if (requestBody != null) {
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        log.info("request body: {}", buffer.readUtf8());
      }

      Response response = chain.proceed(request);

      log.info(
          "<=== Received response code is {} , response headers: {}",
          response.code(),
          response.headers());

      ResponseBody responseBody = null;
      try {
        responseBody = response.peekBody(Long.MAX_VALUE);
        log.info("response body:{}", responseBody.string());
      } catch (Exception e) {
        log.warn("response body is null : {}", e.getMessage());
      }

      return response;
    }
  }
}

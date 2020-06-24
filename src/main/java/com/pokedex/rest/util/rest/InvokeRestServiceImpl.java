package com.pokedex.rest.util.rest;

import com.pokedex.rest.util.rest.trustmanager.TrustAllCerts;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;


@Service
public class InvokeRestServiceImpl implements InvokeRestService {

    private static final Logger LOG = LoggerFactory.getLogger(InvokeRestServiceImpl.class);
    private static final String TLS12 = "TLSv1.2";

    private HttpHeaders headers;

    private TrustManager[] trustManager;

    public InvokeRestServiceImpl() {
        this.headers = getDefaultHeaders();
        this.trustManager = TrustAllCerts.getTrustManager();
    }

    private HttpHeaders getDefaultHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("user-agent", "Mozilla/5.0 Firefox/26.0");
        return httpHeaders;
    }


    @Override
    public <T, R> R invokeRestService(final String endpoint, HttpMethod method, T request, Class<R> expectedResponse) {
        RestTemplate rest = new RestTemplate();
        try {
            TrustManager[] trustAllCerts = this.trustManager;
            SSLContext context = SSLContext.getInstance(TLS12);
            context.init(null, trustAllCerts, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

            rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            LOG.error("Error durante la configuración de TLSv1.2: ", e);
        }
        HttpEntity<T> httpRequest = new HttpEntity<>(request, this.headers);
        ResponseEntity<R> response = rest.exchange(endpoint, method, httpRequest, expectedResponse);

        if (response.getStatusCode().is2xxSuccessful()) {
            LOG.info("[invokeRestService] -> Call to endpoint {} was successful.", endpoint);
            return response.getBody();
        } else {
            LOG.info("[invokeRestService] -> Call to endpoint {} was unsuccessful. HTTP status code -> {}.", endpoint, response.getStatusCode());
            return null;
        }
    }
}

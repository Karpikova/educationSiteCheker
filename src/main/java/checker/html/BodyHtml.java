package checker.html;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BodyHtml {

    private final static Logger LOGGER = LogManager.getLogger(BodyHtml.class);
    private final HttpClient client;
    private final String url;

    public BodyHtml(HttpClient client, String url) {
        this.client = client;
        this.url = url;
    }

    public String body() throws Exception {
        LOGGER.info("Вызываю " + url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        if (response.statusCode() == 200) {
            return body;
        } else {
            int sc = response.statusCode();
            System.out.println("Ошибка API: " + sc);
            throw new Exception(String.valueOf(sc));
        }
    }
}

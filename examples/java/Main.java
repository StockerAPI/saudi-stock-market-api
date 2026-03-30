import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Main {
    private static final String TOKEN = "YOUR_TOKEN";
    private static final String HTTP_BASE = "https://api.kun.pro";
    private static final String WS_URL = "wss://ws.kun.pro/ws?token=" + TOKEN;
    private static final String MARKET = "SA";
    private static final String EXCHANGE = "TADAWUL";
    private static final String SYMBOL = "TADAWUL:000040";

    public static void main(String[] args) throws Exception {
        callHistory();
        callExchange();
        streamRealtime();
    }

    private static void callHistory() throws Exception {
        String url = HTTP_BASE + "/api/history?market=" + enc(MARKET)
                + "&symbol=" + enc(SYMBOL)
                + "&interval=1&count=200&token=" + enc(TOKEN);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .timeout(Duration.ofSeconds(15))
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("history status=" + response.statusCode());
        System.out.println(response.body());
    }

    private static void callExchange() throws Exception {
        String url = HTTP_BASE + "/api/exchange?market=" + enc(MARKET)
                + "&venue=" + enc(EXCHANGE)
                + "&tickers=000040,005930"
                + "&token=" + enc(TOKEN);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .timeout(Duration.ofSeconds(15))
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("exchange status=" + response.statusCode());
        System.out.println(response.body());
    }

    private static void streamRealtime() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<Void> done = new CompletableFuture<>();

        WebSocket ws = client.newWebSocketBuilder()
                .buildAsync(URI.create(WS_URL), new WebSocket.Listener() {
                    @Override
                    public void onOpen(WebSocket webSocket) {
                        String subscribeJson = "{\"action\":\"subscribe\",\"market\":\"" + MARKET
                                + "\",\"exchange\":\"" + EXCHANGE
                                + "\",\"symbols\":[\"TADAWUL:000040\",\"TADAWUL:005930\"],\"replay\":\"last\"}";
                        webSocket.sendText(subscribeJson, true);
                        webSocket.request(1);
                    }

                    @Override
                    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                        System.out.println("ws => " + data);
                        webSocket.request(1);
                        return null;
                    }

                    @Override
                    public void onError(WebSocket webSocket, Throwable error) {
                        done.completeExceptionally(error);
                    }
                }).join();

        Thread.sleep(15000);
        ws.sendClose(WebSocket.NORMAL_CLOSURE, "bye").join();
        done.complete(null);
    }

    private static String enc(String input) {
        return URLEncoder.encode(input, StandardCharsets.UTF_8);
    }
}

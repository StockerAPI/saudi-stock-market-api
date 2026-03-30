using System;
using System.Net.Http;
using System.Net.WebSockets;
using System.Text;
using System.Text.Json;
using System.Threading;
using System.Threading.Tasks;

class Program
{
    private const string Token = "YOUR_TOKEN";
    private const string HttpBase = "https://api.kun.pro";
    private const string WsUrl = "wss://ws.kun.pro/ws?token=" + Token;
    private const string Market = "SA";
    private const string Exchange = "TADAWUL";
    private const string Symbol = "TADAWUL:000040";

    static async Task Main()
    {
        await CallHistory();
        await CallExchange();
        await StreamRealtime();
    }

    static async Task CallHistory()
    {
        using var client = new HttpClient();
        var url = $"{HttpBase}/api/history?market={Uri.EscapeDataString(Market)}&symbol={Uri.EscapeDataString(Symbol)}&interval=1&count=200&token={Uri.EscapeDataString(Token)}";

        var response = await client.GetAsync(url);
        var body = await response.Content.ReadAsStringAsync();
        Console.WriteLine($"history status={(int)response.StatusCode}");
        Console.WriteLine(body);
    }

    static async Task CallExchange()
    {
        using var client = new HttpClient();
        var url = $"{HttpBase}/api/exchange?market={Uri.EscapeDataString(Market)}&venue={Uri.EscapeDataString(Exchange)}&tickers=000040,005930&token={Uri.EscapeDataString(Token)}";

        var response = await client.GetAsync(url);
        var body = await response.Content.ReadAsStringAsync();
        Console.WriteLine($"exchange status={(int)response.StatusCode}");
        Console.WriteLine(body);
    }

    static async Task StreamRealtime()
    {
        using var ws = new ClientWebSocket();
        await ws.ConnectAsync(new Uri(WsUrl), CancellationToken.None);

        var subscribe = new
        {
            action = "subscribe",
            market = Market,
            exchange = Exchange,
            symbols = new[] { "TADAWUL:000040", "TADAWUL:005930" },
            replay = "last"
        };

        var payload = JsonSerializer.Serialize(subscribe);
        var bytes = Encoding.UTF8.GetBytes(payload);
        await ws.SendAsync(bytes, WebSocketMessageType.Text, true, CancellationToken.None);

        var buffer = new byte[8192];
        for (int i = 0; i < 3; i++)
        {
            var result = await ws.ReceiveAsync(buffer, CancellationToken.None);
            var text = Encoding.UTF8.GetString(buffer, 0, result.Count);
            Console.WriteLine("ws => " + text);
        }

        await ws.CloseAsync(WebSocketCloseStatus.NormalClosure, "bye", CancellationToken.None);
    }
}

const token = "YOUR_TOKEN";
const httpBase = "https://api.kun.pro";
const wsURL = `wss://ws.kun.pro/ws?token=${encodeURIComponent(token)}`;

const market = "SA";
const exchange = "TADAWUL";
const symbol = "TADAWUL:000040";

async function callHistory() {
  const url = `${httpBase}/api/history?market=${encodeURIComponent(market)}&symbol=${encodeURIComponent(symbol)}&interval=1&count=200&token=${encodeURIComponent(token)}`;
  const res = await fetch(url);
  const text = await res.text();
  console.log("history status", res.status);
  console.log(text);
}

async function callExchange() {
  const url = `${httpBase}/api/exchange?market=${encodeURIComponent(market)}&venue=${encodeURIComponent(exchange)}&tickers=000040,005930&token=${encodeURIComponent(token)}`;
  const res = await fetch(url);
  const text = await res.text();
  console.log("exchange status", res.status);
  console.log(text);
}

function streamRealtime() {
  const ws = new WebSocket(wsURL);

  ws.addEventListener("open", () => {
    ws.send(
      JSON.stringify({
        action: "subscribe",
        market,
        exchange,
        symbols: ["TADAWUL:000040", "TADAWUL:005930"],
        replay: "last",
      })
    );
  });

  ws.addEventListener("message", (event) => {
    console.log("ws =>", event.data);
  });

  ws.addEventListener("error", (event) => {
    console.error("ws error", event);
  });

  setTimeout(() => ws.close(), 15000);
}

(async () => {
  await callHistory();
  await callExchange();
  streamRealtime();
})();

# Saudi stock market API

Real-time Saudi stock market API with WebSocket, OHLCV, and snapshot endpoints for Tadawul.

## Build With Saudi Arabia Market Data

Kun Data provides a token-authenticated API stack for developers who need Saudi Arabia market data in products that must load fast, stream live prices, and support charting or screening workflows.

This repository is focused on Saudi Arabia coverage with support for TADAWUL and is designed for teams building broker dashboards, watchlists, quant tools, stock screeners, mobile apps, and market overview pages.

## Common Integration Needs

Most teams integrating market data need one or more of these capabilities:

- Real-time Saudi Arabia stock market API
- WebSocket streaming quotes
- Historical OHLCV or candlestick data
- Snapshot data for market movers, rankings, and watchlists
- Coverage for TADAWUL

## What You Get

| Capability | What it is used for |
| --- | --- |
| Real-time WebSocket API | Live prices, incremental updates, ticker streams, dashboard refreshes |
| Historical OHLCV API | Charts, backfill, analytics, historical candles, K-line data |
| Exchange snapshot API | Watchlists, overview pages, leaderboard modules, quote batches |
| Token-based auth | Access control for HTTP and WebSocket integrations |
| Market and exchange filters | Query by `market`, `exchange` or `venue`, `symbol`, and `symbols` |

## Product Fit

This API model is suitable for:

- Trading dashboards
- Market overview products
- Watchlists and screeners
- Broker or internal finance tools
- Mobile investing apps
- Quant research and monitoring systems

## Coverage

Supported exchanges for this repository:

- `TADAWUL`


Primary market code:

- `SA`

## Integration Pattern

The current API stack follows a straightforward model:

- Use a token for authenticated access
- Use HTTP for initialization and historical backfill
- Use the snapshot endpoint for batched market views
- Use WebSocket for live market updates after the first page load

## Technical Notes

- Historical requests use a single `symbol` in `EXCHANGE:TICKER` format
- Historical candles support `interval` and `count`
- Snapshot requests support `market`, `venue`, `symbol`, `symbols`, `ticker`, and `tickers`
- WebSocket subscriptions support market-wide, exchange-level, and symbol-level subscriptions
- HTTP requests can use bearer authentication and WebSocket connections use token query auth

## Links

- Website: https://kun.pro/stocks-en.html
- Docs: https://kun.pro/docs-en.html
- Main site: https://kun.pro

## FAQ

### Does this support real-time Saudi Arabia market data?

Yes. The API stack includes WebSocket streaming for live price updates and realtime market refresh workflows.

### Does this include historical candles or OHLCV data?

Yes. Historical market data supports candle-based retrieval for charting, analytics, and backfill use cases.

### Can I query by exchange or symbol?

Yes. The available endpoints support filtering by market and exchange, and symbol-level queries are supported where documented.

### What kind of products is this repository targeting?

It is aimed at developers building charting products, watchlists, screeners, financial dashboards, market overview pages, and other applications that need structured market data.

## Evaluate The API

If you need Saudi Arabia market data coverage for TADAWUL, start with the product page and documentation:

- Product access: https://kun.pro/stocks-en.html
- Developer docs: https://kun.pro/docs-en.html

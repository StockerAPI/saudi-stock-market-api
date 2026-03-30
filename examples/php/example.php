<?php

$token = 'YOUR_TOKEN';
$httpBase = 'https://api.kun.pro';
$wsUrl = 'wss://ws.kun.pro/ws?token=' . rawurlencode($token);
$market = 'SA';
$exchange = 'TADAWUL';
$symbol = 'TADAWUL:000040';

function httpGet(string $url): void
{
    $ch = curl_init($url);
    curl_setopt_array($ch, [
        CURLOPT_RETURNTRANSFER => true,
        CURLOPT_TIMEOUT => 15,
    ]);

    $body = curl_exec($ch);
    $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    if ($body === false) {
        echo 'curl error: ' . curl_error($ch) . PHP_EOL;
    } else {
        echo "status={$status}" . PHP_EOL;
        echo $body . PHP_EOL;
    }
    curl_close($ch);
}

$historyUrl = sprintf(
    '%s/api/history?market=%s&symbol=%s&interval=1&count=200&token=%s',
    $httpBase,
    rawurlencode($market),
    rawurlencode($symbol),
    rawurlencode($token)
);

$exchangeUrl = sprintf(
    '%s/api/exchange?market=%s&venue=%s&tickers=000040,005930&token=%s',
    $httpBase,
    rawurlencode($market),
    rawurlencode($exchange),
    rawurlencode($token)
);

echo "=== history ===" . PHP_EOL;
httpGet($historyUrl);

echo "=== exchange ===" . PHP_EOL;
httpGet($exchangeUrl);

echo "=== websocket ===" . PHP_EOL;
echo "Use any WebSocket client to connect: {$wsUrl}" . PHP_EOL;
echo "Then send JSON:" . PHP_EOL;
echo json_encode([
    'action' => 'subscribe',
    'market' => $market,
    'exchange' => $exchange,
    'symbols' => ['TADAWUL:000040', 'TADAWUL:005930'],
    'replay' => 'last',
], JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT) . PHP_EOL;


//go:build ignore
// +build ignore

package main

import (
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"time"

	"github.com/gorilla/websocket"
)

const (
	token      = "YOUR_TOKEN"
	httpBase   = "https://api.kun.pro"
	wsURL      = "wss://ws.kun.pro/ws?token=" + token
	market     = "SA"
	exchange   = "TADAWUL"
	symbol     = "TADAWUL:000040"
	tickerList = "000040,005930"
)

func main() {
	if err := callHistory(); err != nil {
		fmt.Println("history error:", err)
	}
	if err := callExchange(); err != nil {
		fmt.Println("exchange error:", err)
	}
	if err := streamRealtime(); err != nil {
		fmt.Println("ws error:", err)
	}
}

func callHistory() error {
	url := fmt.Sprintf("%s/api/history?market=%s&symbol=%s&interval=1&count=200&token=%s", httpBase, market, symbol, token)
	resp, err := http.Get(url)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	body, _ := io.ReadAll(resp.Body)
	fmt.Printf("history status=%d body=%s\n", resp.StatusCode, string(body))
	return nil
}

func callExchange() error {
	url := fmt.Sprintf("%s/api/exchange?market=%s&venue=%s&tickers=%s&token=%s", httpBase, market, exchange, tickerList, token)
	resp, err := http.Get(url)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	body, _ := io.ReadAll(resp.Body)
	fmt.Printf("exchange status=%d body=%s\n", resp.StatusCode, string(body))
	return nil
}

func streamRealtime() error {
	conn, _, err := websocket.DefaultDialer.Dial(wsURL, nil)
	if err != nil {
		return err
	}
	defer conn.Close()

	sub := map[string]any{
		"action":   "subscribe",
		"market":   market,
		"exchange": exchange,
		"symbols":  []string{"TADAWUL:000040", "TADAWUL:005930"},
		"replay":   "last",
	}

	if err := conn.WriteJSON(sub); err != nil {
		return err
	}

	_ = conn.SetReadDeadline(time.Now().Add(20 * time.Second))
	for i := 0; i < 3; i++ {
		_, msg, err := conn.ReadMessage()
		if err != nil {
			return err
		}
		var pretty any
		if json.Unmarshal(msg, &pretty) == nil {
			b, _ := json.MarshalIndent(pretty, "", "  ")
			fmt.Println(string(b))
			continue
		}
		fmt.Println(string(msg))
	}
	return nil
}

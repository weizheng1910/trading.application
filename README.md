# Trading Application 
## Overview
* This is a simple API that simulates a trading application.
* The application pulls pricing from Binance and Huobi every 10 seconds and stores them in the database. 
* Users are able to:
  * Find the best bid-ask price among two currency pairs BtcUsdt EthUsdt, 
  * Trade based on the best price, 
  * Find their wallet balance, 
  * And also see their trading history. 
* Price aggregation from the source below:
  * Binance Url : https://api.binance.com/api/v3/ticker/bookTicker
  * Houbi Url : https://api.huobi.pro/market/tickers

## Methodology
* This app is developed using TDD, starting from the database layer to the application layer. 

#### Roadmap
-[ ] Standardize response body
# Trading Application 

Price aggregation from the source below:
Binance
Url : https://api.binance.com/api/v3/ticker/bookTicker
Houbi
Url : https://api.huobi.pro/market/tickers
Create a 10 seconds interval scheduler to retrieve the pricing from the source
above and store the best pricing into the database.
Hints: Bid Price use for SELL order, Ask Price use for BUY order
2. Create an api to retrieve the latest best aggregated price.
3. Create an api which allows users to trade based on the latest best aggregated
price.
Remarks: Do not integrate with other third party system
4. Create an api to retrieve the user’s crypto currencies wallet balance
5. Create an api to retrieve the user trading history.

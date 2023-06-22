package kashkoala;

import java.io.Serializable;
import java.math.BigDecimal;

import yahoofinance.Stock;



public class Tuple implements Serializable{
	
    private static final long serialVersionUID = 1L;

	private String ticker;
	private BigDecimal purchased_price;
	private int shares;
	public Tuple(String ticker, BigDecimal price, int shares_to_buy) {
		this.ticker = ticker;
		this.purchased_price = price;
		this.shares = shares_to_buy;
		
		}
	
	public String getTicker() {
		return this.ticker;
	}
	public BigDecimal getPurchasedPrice() {
		return this.purchased_price;
	}

	public int getShares() {
		return this.shares;
	}
	
	public void setShares(int new_shares) {
		this.shares = this.shares+new_shares;
		
	}
	public void sellShares(int sold) {
		this.shares = this.shares-sold;
	}
	
	public void setPurchasedPrice(double new_price) {
		this.purchased_price = BigDecimal.valueOf(new_price);
	}



}

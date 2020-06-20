package zpi.aouu.sales;

import java.math.BigDecimal;
import java.util.Map;

public class Currency {
	public String success;
	public BigDecimal timestamp;
	public String base;
	public String date;
	public Map<String, Double> rates;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public BigDecimal getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(BigDecimal timestamp) {
		this.timestamp = timestamp;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Map<String, Double> getRates() {
		return rates;
	}

	public void setRates(Map<String, Double> rates) {
		this.rates = rates;
	}
}

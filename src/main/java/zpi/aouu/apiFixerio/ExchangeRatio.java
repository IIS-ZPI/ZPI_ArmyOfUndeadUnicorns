package zpi.aouu.apiFixerio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import zpi.aouu.sales.CountryData;
import zpi.aouu.sales.Currency;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//api using base price as EUR, we need to convert it to USD

public class ExchangeRatio {

	private Map<CountryData, Double> countryDataAndExchangeList;
	private String basePrice;

	public ExchangeRatio(List<CountryData> countryDataList, String basePrice) {
		countryDataAndExchangeList = new HashMap<>();
		for (CountryData countryData : countryDataList) {
			this.countryDataAndExchangeList.put(countryData, 1.0);
		}
		this.basePrice = basePrice;
		Currency currency = setCorrectlyValues(basePrice);
		for (Map.Entry<CountryData, Double> entry : countryDataAndExchangeList.entrySet()) {
			entry.setValue(currency.rates.get(entry.getKey().currency));
		}
		System.out.println();
	}

	public Map<CountryData, Double> getCountryDataAndExchangeList() {
		return countryDataAndExchangeList;
	}

	private Currency setCorrectlyValues(String basePrice) {
		URL url = null;
		try {
			url = new URL("http://data.fixer.io/api/latest?access_key="+System.getenv("API_FIXERIO")+"&base=EUR&symbols=" + basePrice + "," + countryDataAndExchangeList.keySet().stream()
					.map(key -> key.currency + ',')
					.collect(Collectors.joining()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Type listType = new TypeToken<Currency>() {
		}.getType();
		Currency currency = new Gson().fromJson(reader, listType);
		double usd = currency.rates.get("USD");
		for (Map.Entry<String, Double> entry : currency.rates.entrySet()) {
			entry.setValue(entry.getValue() / usd);
		}
		return currency;
	}

}

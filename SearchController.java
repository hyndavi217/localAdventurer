package webtech.umd.localadv;





import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SearchController extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		JSONObject json = new JSONObject();
		PrintWriter pw = response.getWriter();
		HttpSession session = request.getSession();
		
		String finalSearch = request.getParameter("search");
		String dateFrom = request.getParameter("from");
		String dateTo = request.getParameter("to");
		String geoLocation = request.getParameter("geoLocation");
		String country = request.getParameter("country");
		String numberResult = request.getParameter("page");
		String domains = request.getParameter("domains");
		String excludeDomains = request.getParameter("excludeDomains");
		String sortBy = request.getParameter("sortBy");

		String key = "cbea8e0e969849f39c5ff1ca7f776b14";
		finalSearch = finalSearch.replaceAll("(\n)", ", ");
		String url = "https://newsapi.org/v2/everything?q=(" + finalSearch + ")&apiKey=" + key;
		if (dateFrom != "") {
			url += "&from=" + dateFrom;
		}
		if (dateFrom != "" && dateTo != "") {
			url += "&to=" + dateTo;
		}
		if (geoLocation != null && geoLocation != "") {
			url += "&language=" + geoLocation;
		}
		if (!country.equals("off")) {
			url += "&country=" + country;
		}
		if (numberResult != null) {
			url += "&pagesize=" + numberResult;
		}
		if (domains != "") {
			url += "&domains=" + domains;
		}
		if (excludeDomains != "") {
			url += "&excludedomains=" + excludeDomains;
		}
		if (!sortBy.equals("PublishedAt")) {
			url += "&sortBy=" + sortBy;
		}

		URL finalUrl = new URL(url);
		System.setProperty("http.agent", "Chrome");
		System.setProperty("https.agent", "Chrome");
		HttpURLConnection myURLConnection = (HttpURLConnection) finalUrl.openConnection();
		myURLConnection.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		myURLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
		myURLConnection.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
		myURLConnection.setRequestProperty("Cache-Control", "max-age=0");
		myURLConnection.setRequestProperty("Connection", "keep-alive");
		myURLConnection.setRequestProperty("Host", "newsapi.org");
		myURLConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
		myURLConnection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
		myURLConnection.setDoOutput(true);

		String xmlData = IOUtils.toString(myURLConnection.getInputStream(), "UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(xmlData);
		try {
			json.put("data", node);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		session.setAttribute("searchData", node);
		/*
		 * catch (JSONException e) {
		 * System.out.println("Error to connecting site: " + e.getMessage()); }
		 */

		response.setContentType("application/json");
		pw.write(json.toString());
	}
}

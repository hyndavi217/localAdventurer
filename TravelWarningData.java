package webtech.umd.localadv;





import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import org.json.JSONObject;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TravelWarningData extends HttpServlet {

	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		JSONObject json = new JSONObject();
		PrintWriter out = response.getWriter();
		try {
			String countryCode = request.getParameter("countryCode");
			String url = "https://www.reisewarnung.net/api?country=" + countryCode;
			URL finalUrl = new URL(url);
			System.setProperty("http.agent", "Chrome");
			System.setProperty("https.agent", "Chrome");
			HttpURLConnection myURLConnection = (HttpURLConnection) finalUrl.openConnection();
			myURLConnection.setRequestProperty("Cache-Control", "max-age=0");
			myURLConnection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
			myURLConnection.setDoOutput(true);
			String jsonData = IOUtils.toString(myURLConnection.getInputStream(), "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(jsonData);
			json.put("data", node);

		} catch (Exception e) {
		}
		out.write(json.toString());
	}
}

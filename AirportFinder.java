package webtech.umd.localadv;








import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
/*import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;*/
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AirportFinder extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		JSONObject json = new JSONObject();
		PrintWriter out = response.getWriter();
		
			String cityName = request.getParameter("cityName");
			String url = "https://cometari-airportsfinder-v1.p.rapidapi.com/api/airports/by-text?text=" + cityName;
			URL finalUrl = new URL(url);
			System.setProperty("http.agent", "Chrome");
			System.setProperty("https.agent", "Chrome");
			HttpURLConnection myURLConnection = (HttpURLConnection) finalUrl.openConnection();
			myURLConnection.setRequestProperty("Cache-Control", "max-age=0");
			myURLConnection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
			myURLConnection.setRequestProperty("X-RapidAPI-Key", "5c7a8e985bmsh7050f00282d69a9p1c63f8jsn3639d77503a7");
			myURLConnection.setDoOutput(true); // using url connection to output
			 InputStream is= myURLConnection.getInputStream();
			
			try {
			String jsonData = IOUtils.toString(is, "UTF-8"); // charset
			/*String jsonData = IOUtils.toString(myURLConnection.getInputStream(), Charset.forName("UTF-8"));*/
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(jsonData);
			json.put("data", node);

		} catch (Exception e) {
			e.printStackTrace();
			
		}
		out.write(json.toString());
	}
}


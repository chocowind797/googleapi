package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class GooglePlace {

	private static final String URL1 = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";

	private static final String URL2 = "&radius=500&types=food&name=";

	private static final String URL3 = "&language=zh-TW&key=AIzaSyBx_bJO95qyl507jRQEkTjZHxrCRb6Y3R0";

	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("輸入想吃的東西：");
		String url_temp = scanner.nextLine();
		System.out.println("輸入經度(後4位)：");
		String url_lng = scanner.nextLine();
		System.out.println("輸入緯度(後4位)：");
		String url_lat = scanner.nextLine();
		System.out.println("==============================");
		String google_URL = String.format("%s%s,%s%s%s%s", URL1, url_lat, url_lng, URL2, url_temp, URL3);
		URL url = new URL(google_URL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("GET");
		con.setUseCaches(false);
		con.setDoInput(true);

		InputStream is = con.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		StringBuffer sb = new StringBuffer();
		String str;

		while ((str = br.readLine()) != null) {
			sb.append(str);
		}

		br.close();
		isr.close();
		is.close();
		con.disconnect();
		scanner.close();

		String yes = new JSONObject(sb.toString()).getString("status");
		if (yes.equalsIgnoreCase("ok")) {
			JSONArray a1 = new JSONObject(sb.toString()).getJSONArray("results");
			for (int i = 0; i < a1.length(); i++) {
				JSONObject data = a1.getJSONObject(i);
				String name = data.getString("name");
				double lat = data.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
				double lng = data.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
				float rating = data.getFloat("rating");

				int rating_user = data.getInt("user_ratings_total");
				String vicinity = data.getString("vicinity");

				System.out.println("商家名稱：" + name);

				if (data.has("opening_hours")) {
					boolean open = data.getJSONObject("opening hours").getBoolean("open_now");
					System.out.println("商家" + (open ? "營業中" : "未營業"));
				} else
					System.out.println("無此商家是否營業中之資料");

				System.out.println("商家評分: " + rating);

				System.out.println("評分人數: " + rating_user);

				System.out.printf("商家經緯度\t經度: %.4f緯度\t: %.4f\n", lng, lat);

				System.out.println("商家地址: " + vicinity);

				System.out.println("=============================");
			}
		} else {
			System.out.println("無" + url_temp + "資料");
		}
	}
}

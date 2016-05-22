package delion.lobiapi.HttpAPI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import delion.lobiapi.HttpAPI.Header.GetHeader;
import delion.lobiapi.HttpAPI.Header.PostHeader;

public class Http {
	
	public CookieManager cookie = null;
	
	public Http(){
		this.cookie = new CookieManager();
		CookieHandler.setDefault(this.cookie);
	}
	
	public String get(String url, GetHeader header){
		String result = "";
		try {
			URI uri = new URI(url);
			HttpURLConnection connection = (HttpURLConnection)uri.toURL().openConnection();
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("GET");
			if(header.Host != "")
				connection.setRequestProperty("Host", header.Host);
			if(header.Connection)
				connection.setRequestProperty("Connection", "Keep-Alive");
			if(header.Accept != "")
				connection.setRequestProperty("Accept", header.Accept);
			if(header.UserAgent != "")
				connection.setRequestProperty("User-Agent", header.UserAgent);
			if(header.Referer != "")
				connection.setRequestProperty("Referer", header.Referer);
			if(header.AcceptEncoding != "")
				connection.setRequestProperty("Accept-Encoding", header.AcceptEncoding);
			if(header.AcceptLanguage != "")
				connection.setRequestProperty("Accept-Language", header.AcceptLanguage);
			
			String location = connection.getHeaderField("Location");
			if(location != null){
				GetHeader redirect_header = new GetHeader();
				if(header.Host != "")
					redirect_header.setHost(header.Host);
				if(header.Connection)
					redirect_header.setConnection(header.Connection);
				if(header.Accept != "")
					redirect_header.setAccept(header.Accept);
				if(header.UserAgent != "")
					redirect_header.setUserAgent(header.UserAgent);
				if(header.Referer != "")
					redirect_header.setReferer(header.Referer);
				if(header.AcceptEncoding != "")
					redirect_header.setAcceptEncoding(header.AcceptEncoding);
				if(header.AcceptLanguage != "")
					redirect_header.setAcceptLanguage(header.AcceptLanguage);
				result = this.get((location.indexOf("http") != 0 ? url.substring(0, url.lastIndexOf("/")) : "") + location, redirect_header);
			}
			else{
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				String buffer = reader.readLine();
				while(buffer != null){
					result += buffer + "\n";
					buffer = reader.readLine();
				}
			}
		} catch (URISyntaxException e) {
			
		} catch (MalformedURLException e) {
			
		} catch (IOException e) {
			
		}
		return result;
	}
	
	public String post(String url, String data, PostHeader header){
		String result = "";
		try {
			byte[] post_data = data.getBytes("UTF-8");
			URI uri = new URI(url);
			HttpURLConnection connection = (HttpURLConnection)(uri.toURL().openConnection());
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			if(header.Host != "")
				connection.setRequestProperty("Host", header.Host);
			if(header.Connection)
				connection.setRequestProperty("Connection", "Keep-Alive");
			if(header.Accept != "")
				connection.setRequestProperty("Accept", header.Accept);
			if(header.Origin != "")
				connection.setRequestProperty("Origin", header.Origin);
			if(header.UserAgent != "")
				connection.setRequestProperty("User-Agent", header.UserAgent);
			if(header.ContentType != "")
				connection.setRequestProperty("Content-Type", header.ContentType);
			if(header.Referer != "")
				connection.setRequestProperty("Referer", header.Referer);
			if(header.AcceptEncoding != "")
				connection.setRequestProperty("Accept-Encoding", header.AcceptEncoding);
			if(header.AcceptLanguage != "")
				connection.setRequestProperty("Accept-Language", header.AcceptLanguage);
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
			writer.write(data);
			writer.close();
			
			String location = connection.getHeaderField("Location");
			if(location != null){
				GetHeader redirect_header = new GetHeader();
				if(header.Host != "")
					redirect_header.setHost(header.Host);
				if(header.Connection)
					redirect_header.setConnection(header.Connection);
				if(header.Accept != "")
					redirect_header.setAccept(header.Accept);
				if(header.UserAgent != "")
					redirect_header.setUserAgent(header.UserAgent);
				if(header.Referer != "")
					redirect_header.setReferer(header.Referer);
				if(header.AcceptEncoding != "")
					redirect_header.setAcceptEncoding(header.AcceptEncoding);
				if(header.AcceptLanguage != "")
					redirect_header.setAcceptLanguage(header.AcceptLanguage);
				result = this.get((location.indexOf("http") != 0 ? url.substring(0, url.lastIndexOf("/")) : "") + location, redirect_header);
			}
			else{
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				String buffer = reader.readLine();
				while(buffer != null){
					result += buffer + "\n";
					buffer = reader.readLine();
				}
			}
		} catch (URISyntaxException e) {
			result = "error1";
		} catch (MalformedURLException e) {
			result = "error2";
		} catch (IOException e) {
			result = "error3";
		}
		return result;
	}
	
	public String post_x_www_form_urlencoded(String url, String data, PostHeader header){
		String result = "";
		try {
			byte[] data_bytes = data.getBytes("UTF-8");
			URI uri = new URI(url);
			HttpURLConnection connection = (HttpURLConnection)(uri.toURL().openConnection());
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			if(header.Host != "")
				connection.setRequestProperty("Host", header.Host);
			if(header.Connection)
				connection.setRequestProperty("Connection", "Keep-Alive");
			if(header.Accept != "")
				connection.setRequestProperty("Accept", header.Accept);
			if(header.Origin != "")
				connection.setRequestProperty("Origin", header.Origin);
			if(header.UserAgent != "")
				connection.setRequestProperty("User-Agent", header.UserAgent);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			if(header.Referer != "")
				connection.setRequestProperty("Referer", header.Referer);
			if(header.AcceptEncoding != "")
				connection.setRequestProperty("Accept-Encoding", header.AcceptEncoding);
			if(header.AcceptLanguage != "")
				connection.setRequestProperty("Accept-Language", header.AcceptLanguage);
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
			writer.write(data);
			writer.close();
			
			String location = connection.getHeaderField("Location");
			if(location != null){
				GetHeader redirect_header = new GetHeader();
				if(header.Host != "")
					redirect_header.setHost(header.Host);
				if(header.Connection)
					redirect_header.setConnection(header.Connection);
				if(header.Accept != "")
					redirect_header.setAccept(header.Accept);
				if(header.UserAgent != "")
					redirect_header.setUserAgent(header.UserAgent);
				if(header.Referer != "")
					redirect_header.setReferer(header.Referer);
				if(header.AcceptEncoding != "")
					redirect_header.setAcceptEncoding(header.AcceptEncoding);
				if(header.AcceptLanguage != "")
					redirect_header.setAcceptLanguage(header.AcceptLanguage);
				result = this.get((location.indexOf("http") != 0 ? url.substring(0, url.lastIndexOf("/")) : "") + location, redirect_header);
			}
			else{
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				String buffer = reader.readLine();
				while(buffer != null){
					result += buffer + "\n";
					buffer = reader.readLine();
				}
			}
		} catch (URISyntaxException e) {
			result = "error1";
		} catch (MalformedURLException e) {
			result = "error2";
		} catch (IOException e) {
			result = "error3";
		}
		return result;
	}
	
	public String post_form_data(String url, String boundary, String[] data, PostHeader header){
		String result = "";
		try {
			String post_data = "";
			for(String cdata : data)
				post_data += "--" + boundary + "\r\n" + cdata + "\r\n";
			
			byte[] data_bytes = post_data.getBytes("UTF-8");
			URI uri = new URI(url);
			HttpURLConnection connection = (HttpURLConnection)(uri.toURL().openConnection());
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			if(header.Host != "")
				connection.setRequestProperty("Host", header.Host);
			if(header.Connection)
				connection.setRequestProperty("Connection", "Keep-Alive");
			if(header.Accept != "")
				connection.setRequestProperty("Accept", header.Accept);
			if(header.Origin != "")
				connection.setRequestProperty("Origin", header.Origin);
			if(header.UserAgent != "")
				connection.setRequestProperty("User-Agent", header.UserAgent);
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			if(header.Referer != "")
				connection.setRequestProperty("Referer", header.Referer);
			if(header.AcceptEncoding != "")
				connection.setRequestProperty("Accept-Encoding", header.AcceptEncoding);
			if(header.AcceptLanguage != "")
				connection.setRequestProperty("Accept-Language", header.AcceptLanguage);
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
			writer.write(post_data);
			writer.close();
			
			String location = connection.getHeaderField("Location");
			if(location != null){
				GetHeader redirect_header = new GetHeader();
				if(header.Host != "")
					redirect_header.setHost(header.Host);
				if(header.Connection)
					redirect_header.setConnection(header.Connection);
				if(header.Accept != "")
					redirect_header.setAccept(header.Accept);
				if(header.UserAgent != "")
					redirect_header.setUserAgent(header.UserAgent);
				if(header.Referer != "")
					redirect_header.setReferer(header.Referer);
				if(header.AcceptEncoding != "")
					redirect_header.setAcceptEncoding(header.AcceptEncoding);
				if(header.AcceptLanguage != "")
					redirect_header.setAcceptLanguage(header.AcceptLanguage);
				result = this.get((location.indexOf("http") != 0 ? url.substring(0, url.lastIndexOf("/")) : "") + location, redirect_header);
			}
			else{
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				String buffer = reader.readLine();
				while(buffer != null){
					result += buffer + "\n";
					buffer = reader.readLine();
				}
			}
		} catch (URISyntaxException e) {
			result = "error1";
		} catch (MalformedURLException e) {
			result = "error2";
		} catch (IOException e) {
			result = "error3";
		}
		return result;
	}
}

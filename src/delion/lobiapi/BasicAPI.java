package delion.lobiapi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import delion.lobiapi.HttpAPI.Http;
import delion.lobiapi.HttpAPI.Header.GetHeader;
import delion.lobiapi.HttpAPI.Header.PostHeader;

public class BasicAPI {
	private Http NetworkAPI = null;

	public BasicAPI(){
		this.NetworkAPI = new Http();
	}

	public boolean Login(String mail, String password){
		GetHeader header1 = new GetHeader()
				.setHost("lobi.co")
				.setConnection(true)
				.setAccept("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
				.setAcceptLanguage("ja,en-US;q=0.8,en;q=0.6");
		String source = this.NetworkAPI.get("https://lobi.co/signin", header1);
		String csrf_token = Pattern.get_string(source, Pattern.csrf_token, "\"");

		String post_data = String.format("csrf_token=%s&email=%s&password=%s", csrf_token, mail, password);
		PostHeader header2 = new PostHeader()
                .setHost("lobi.co")
                .setConnection(true)
                .setAccept("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
                .setAcceptLanguage("ja,en-US;q=0.8,en;q=0.6")
                .setOrigin("https://lobi.co")
                .setReferer("https://lobi.co/signin");

		String result = this.NetworkAPI.post_x_www_form_urlencoded("https://lobi.co/signin", post_data, header2);
		return result.indexOf("ログインに失敗しました") == -1;
	}

	public boolean TwitterLogin(String mail, String password){
		GetHeader header1 = new GetHeader()
				.setHost("lobi.co")
				.setConnection(true)
				.setAccept("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
				.setAcceptLanguage("ja,en-US;q=0.8,en;q=0.6");
		String source = this.NetworkAPI.get("https://lobi.co/signup/twitter", header1);
		String authenticity_token = Pattern.get_string(source, Pattern.authenticity_token, "\"");
		String redirect_after_login = Pattern.get_string(source, Pattern.redirect_after_login, "\"");
		String oauth_token = Pattern.get_string(source, Pattern.oauth_token, "\"");

		String post_data = "";
		try {
			post_data = String.format("authenticity_token=%s&redirect_after_login=%s&oauth_token=%s&session%%5Busername_or_email%%5D=%s&session%%5Bpassword%%5D=%s", authenticity_token, URLEncoder.encode(redirect_after_login, "UTF-8"), oauth_token, mail, password);
		} catch (UnsupportedEncodingException e) {
			//URLエンコードに失敗...
		}
		PostHeader header2 = new PostHeader()
				.setHost("api.twitter.com")
				.setConnection(true)
				.setAccept("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
				.setAcceptLanguage("ja,en-US;q=0.8,en;q=0.6")
				.setOrigin("https://api.twitter.com");

		String source2 = this.NetworkAPI.post_x_www_form_urlencoded("https://api.twitter.com/oauth/authorize", post_data, header2);
		if (source2.indexOf("Twitterにログイン") > -1)
			return false;

		return this.NetworkAPI.get(Pattern.get_string(source2, Pattern.twitter_redirect_to_lobi, "\""), header1).indexOf("ログインに失敗しました") == -1;
	}
	
	

	private static class Pattern{
		public static String csrf_token = "<input type=\"hidden\" name=\"csrf_token\" value=\"";
		public static String authenticity_token = "<input name=\"authenticity_token\" type=\"hidden\" value=\"";
		public static String redirect_after_login = "<input name=\"redirect_after_login\" type=\"hidden\" value=\"";
		public static String oauth_token = "<input id=\"oauth_token\" name=\"oauth_token\" type=\"hidden\" value=\"";
		public static String twitter_redirect_to_lobi = "<a class=\"maintain-context\" href=\"";
		
		public static String get_string(String source, String pattern, String end_pattern)
		{
			int start = source.indexOf(pattern) + pattern.length();
			int end = source.indexOf(end_pattern, start + 1);
			return source.substring(start, end);
		}
	}
}

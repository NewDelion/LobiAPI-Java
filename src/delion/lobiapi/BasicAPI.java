package delion.lobiapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import delion.lobiapi.HttpAPI.Http;
import delion.lobiapi.HttpAPI.Header.GetHeader;
import delion.lobiapi.HttpAPI.Header.PostHeader;
import delion.lobiapi.Json.Chat;
import delion.lobiapi.Json.Contacts;
import delion.lobiapi.Json.Followers;
import delion.lobiapi.Json.Group;
import delion.lobiapi.Json.Me;
import delion.lobiapi.Json.Notifications;
import delion.lobiapi.Json.PrivateGroups;
import delion.lobiapi.Json.PublicGroups;
import delion.lobiapi.Json.User;

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
			e.printStackTrace();
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
	
	public Me GetMe(){
		GetHeader header = new GetHeader()
				.setHost("web.lobi.co")
				.setConnection(true)
				.setAccept("application/json, text/plain, */*")
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
				.setAcceptLanguage("ja,en-US;q=0.8,en;q=0.6");
		
		try {
			String result_json = this.NetworkAPI.get("https://web.lobi.co/api/me?fields=premium", header);
			return new ObjectMapper().readValue(result_json, Me.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public PublicGroups GetPublicGroupList(){
		GetHeader header = new GetHeader()
				.setHost("web.lobi.co")
				.setConnection(true)
				.setAccept("application/json, text/plain, */*")
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
				.setAcceptLanguage("ja,en-US;q=0.8,en;q=0.6");
		
		List<PublicGroups> result = new ArrayList<PublicGroups>();
		ObjectMapper mapper = new ObjectMapper();
		int index = 1;
		while(true){
			try {
				List<PublicGroups> pg = mapper.readValue(this.NetworkAPI.get("https://web.lobi.co/api/public_groups?count=1000&page=" + index + "&with_archived=1", header), new TypeReference<List<PublicGroups>>(){});
				index++;
				if(pg.get(0).items.length == 0)
					break;
				result.addAll(pg);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(result.size() > 0)
			return result.get(0);
		return null;
	}
	
	public PrivateGroups GetPrivateGroupList(){
		GetHeader header = new GetHeader()
				.setHost("web.lobi.co")
				.setConnection(true)
				.setAccept("application/json, text/plain, */*")
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
				.setAcceptLanguage("ja,en-US;q=0.8,en;q=0.6");
		
		List<PrivateGroups> result = new ArrayList<PrivateGroups>();
		ObjectMapper mapper = new ObjectMapper();
		int index = 1;
		while(true){
			try {
				List<PrivateGroups> pg = mapper.readValue(this.NetworkAPI.get("https://web.lobi.co/api/groups?count=1000&page=" + index, header), new TypeReference<List<PrivateGroups>>(){});
				index++;
				if(pg.get(0).items.length == 0)
					break;
				result.addAll(pg);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(result.size() > 0)
			return result.get(0);
		return null;
	}
	
	public Notifications GetNotifications(){
		GetHeader header = new GetHeader()
				.setHost("web.lobi.co")
				.setConnection(true)
				.setAccept("application/json, text/plain, */*")
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
				.setAcceptLanguage("ja,en-US;q=0.8,en;q=0.6");
		
		try {
			return new ObjectMapper().readValue(this.NetworkAPI.get("https://web.lobi.co/api/info/notifications?platform=any&last_cursor=0", header), Notifications.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Contacts GetContacts(String uid){
		GetHeader header = new GetHeader()
				.setHost("web.lobi.co")
				.setConnection(true)
				.setAccept("application/json, text/plain, */*")
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
				.setAcceptLanguage("ja,en-US;q=0.8,en;q=0.6");
		
		try {
			return new ObjectMapper().readValue(this.NetworkAPI.get("https://web.lobi.co/api/user/" + uid + "/contacts", header), Contacts.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Followers GetFollowers(String uid){
		GetHeader header = new GetHeader()
				.setHost("web.lobi.co")
				.setConnection(true)
				.setAccept("application/json, text/plain, */*")
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
				.setAcceptLanguage("ja,en-US;q=0.8,en;q=0.6");
		
		try {
			return new ObjectMapper().readValue(this.NetworkAPI.get("https://web.lobi.co/api/user/" + uid + "/followers", header), Followers.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Group GetGroup(String uid){
		GetHeader header = new GetHeader()
				.setHost("web.lobi.co")
				.setConnection(true)
				.setAccept("application/json, text/plain, */*")
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
				.setAcceptLanguage("ja,en-US;q=0.8,en;q=0.6");
		
		try {
			return new ObjectMapper().readValue(this.NetworkAPI.get("https://web.lobi.co/api/group/" + uid + "?error_flavor=json2&fields=group_bookmark_info%2Capp_events_info", header), Group.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int GetGroupMembersCount(String uid){
		GetHeader header = new GetHeader()
				.setHost("web.lobi.co")
				.setConnection(true)
				.setAccept("application/json, text/plain, */*")
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
				.setAcceptLanguage("ja,en-US;q=0.8,en;q=0.6");
		
		try {
			Integer result = new ObjectMapper().readValue(this.NetworkAPI.get("https://web.lobi.co/api/group/" + uid + "?error_flavor=json2&fields=group_bookmark_info%2Capp_events_info", header), Group.class).members_count;
			return result == null ? 0 : (int)result;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public User[] GetGroupMembers(String uid){
		GetHeader header = new GetHeader()
				.setHost("web.lobi.co")
				.setConnection(true)
				.setAccept("application/json, text/plain, */*")
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
				.setAcceptLanguage("ja,en-US;q=0.8,en;q=0.6");
		
		List<User> result = new ArrayList<User>();
		String next = "0";
		int limit = 10000;
		while(limit-- > 0){
			try {
				Group g = new ObjectMapper().readValue(this.NetworkAPI.get("https://web.lobi.co/api/group/" + uid + "?members_cursor=" + next, header), Group.class);
				for(User user : g.members)
					result.add(user);
				if(g.members_next_cursor == null)
					break;
				if(g.members_next_cursor == 0)
					break;
				next = g.members_next_cursor.toString();
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return (User[])result.toArray(new User[0]);
	}
	
	public Chat[] GetThread(String uid, int count){
		GetHeader header = new GetHeader()
				.setHost("web.lobi.co")
				.setConnection(true)
				.setAccept("application/json, text/plain, */*")
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36")
				.setAcceptLanguage("ja,en-US;q=0.8,en;q=0.6");
		
		try {
			List<Chat> result = new ObjectMapper().readValue(this.NetworkAPI.get("https://web.lobi.co/api/group/" + uid + "/chats?count=" + count, header), new TypeReference<List<Chat>>(){});
			return (Chat[])result.toArray(new Chat[0]);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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

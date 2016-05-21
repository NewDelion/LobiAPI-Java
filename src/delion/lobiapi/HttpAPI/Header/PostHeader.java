package delion.lobiapi.HttpAPI.Header;

public class PostHeader {
	public String Host;
	public boolean Connection;
	public String Accept;
	public String UserAgent;
	public String Referer;
	public String AcceptEncoding;
	public String AcceptLanguage;
	public String Origin;
	public String ContentType;
	
	public PostHeader setHost(String host){
		this.Host = host;
		return this;
	}
	
	public PostHeader setConnection(boolean connection){
		this.Connection = connection;
		return this;
	}
	
	public PostHeader setAccept(String accept){
		this.Accept = accept;
		return this;
	}
	
	public PostHeader setUserAgent(String useragent){
		this.UserAgent = useragent;
		return this;
	}
	
	public PostHeader setReferer(String referer){
		this.Referer = referer;
		return this;
	}
	
	public PostHeader setAcceptEncoding(String encoding){
		this.AcceptEncoding = encoding;
		return this;
	}
	
	public PostHeader setAcceptLanguage(String language){
		this.AcceptLanguage = language;
		return this;
	}
	
	public PostHeader setOrigin(String origin){
		this.Origin = origin;
		return this;
	}
	
	public PostHeader setContentType(String contenttype){
		this.ContentType = contenttype;
		return this;
	}
}

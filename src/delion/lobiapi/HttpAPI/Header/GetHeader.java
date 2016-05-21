package delion.lobiapi.HttpAPI.Header;

public class GetHeader {
	public String Host;
	public boolean Connection;
	public String Accept;
	public String UserAgent;
	public String Referer;
	public String AcceptEncoding;
	public String AcceptLanguage;
	public String Origin;
	
	public GetHeader setHost(String host){
		this.Host = host;
		return this;
	}
	
	public GetHeader setConnection(boolean connection){
		this.Connection = connection;
		return this;
	}
	
	public GetHeader setAccept(String accept){
		this.Accept = accept;
		return this;
	}
	
	public GetHeader setUserAgent(String useragent){
		this.UserAgent = useragent;
		return this;
	}
	
	public GetHeader setReferer(String referer){
		this.Referer = referer;
		return this;
	}
	
	public GetHeader setAcceptEncoding(String encoding){
		this.AcceptEncoding = encoding;
		return this;
	}
	
	public GetHeader setAcceptLanguage(String language){
		this.AcceptLanguage = language;
		return this;
	}
	
	public GetHeader setOrigin(String origin){
		this.Origin = origin;
		return this;
	}
}

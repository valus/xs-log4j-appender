package biz.stelmach.log;

import io.crossroads.XS;
import io.crossroads.XS.Context;
import io.crossroads.XS.Socket;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import com.google.gson.Gson;


public class XSAppender extends AppenderSkeleton implements Appender {

	private Socket socket;
	private String socketType;
    private String endpoint;
    private String mode;
    private String topic;
    private String identity;
    private static final String PUB_SUB = "pub";
    private static final String PUSH_PULL = "push";
    private static final String CONNECT_MODE = "connect";
    private static final String BIND_MODE = "bind";
    private final Gson gson = new Gson();
    
	public XSAppender() {
		super();
	}
	
	public XSAppender(final Socket socket) {
		super();
		this.socket = socket;
	}
	
	public void close() {
		socket.close();
	}

	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(LoggingEvent le) {
        if (topic != null && PUB_SUB.equals(socketType))
        	socket.sendmsg(topic.getBytes(), 0);
        
        socket.sendmsg(gson.toJson(new EventData(identity, le)).getBytes(), 0);
	}

	@Override
    public void activateOptions() {
    	super.activateOptions();
    	createSocket();
    }
	
	private void createSocket() {
		final Context context = XS.context();
		
		if (PUB_SUB.equals(socketType))
    		socket = context.socket(XS.PUB);
    	else if (PUSH_PULL.equals(socketType))
    		socket = context.socket(XS.PUSH);
    	else 
    		socket = context.socket(XS.PUB);

        if (BIND_MODE.equals(mode))
        	socket.bind(endpoint);
        else if (CONNECT_MODE.equals(mode))
        	socket.connect(endpoint);
        else 
        	socket.connect(endpoint);
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getSocketType() {
		return socketType;
	}

	public void setSocketType(String socketType) {
		this.socketType = socketType;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
}

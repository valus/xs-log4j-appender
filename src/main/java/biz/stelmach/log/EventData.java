package biz.stelmach.log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

public class EventData {
	
	private String id;
	private String fqn;
	private String log;
	private long timestamp;
	private String level;
	private String message;
	private String threadName;
	private String ndc;
	private LocationInfo info;

    public EventData(String id, final LoggingEvent event) {
    	this.id = id;
        fqn = event.getFQNOfLoggerClass();
        log = event.getLoggerName();
        timestamp = event.getTimeStamp();
        level = event.getLevel().toString();
        message = event.getMessage().toString();
        threadName = event.getThreadName();
        ndc = event.getNDC();
        info = event.getLocationInformation();
    }

    public LoggingEvent toLoggingEvent() {
        Logger logger = Logger.getLogger(log);
        
        return new LoggingEvent(fqn, logger, timestamp, 
        		Level.toLevel(level), message, threadName, 
        		null, ndc, info, null);
    }
}


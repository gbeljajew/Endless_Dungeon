package bin.game.util.logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class MyLogger
{
    private static final SimpleDateFormat FORMATER = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");
    private static final String LOG_FILE;
    private static final String WORKPATH;
    
    private static final List<LogListener> LISTENER = new ArrayList<>();
    
    private static final List<Entry> LOG = new ArrayList<>();
    
    private MyLogger(){}; // no constructor for you
    
    // ----- Static -------------------------------------------------
    
    static
    {
        
        URI location;
        String workPath = ".";
        try
        {
            location = MyLogger.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            
            Path path = Paths.get( location).getParent();
            
            workPath = path.toAbsolutePath().toString();
        }
        catch( URISyntaxException ex )
        {
            JOptionPane.showMessageDialog(null, "Workpath creation failed" + ex, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
        WORKPATH = workPath;
        LOG_FILE = workPath +  "/log/log " + FORMATER.format(new Date()) + ".log";
        
        System.out.println(LOG_FILE);
        
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                
                //System.out.println("Logs are saved to " + LOG_FILE);
                if(LOG.isEmpty())
                    return;
                
                
                List<String> lines = LOG.stream()
                        .map(e -> e.toString())
                        .collect(Collectors.toList());
                
                try
                {
                    Path logDir = Paths.get(WORKPATH, "log");
                    
                    if(!Files.exists(logDir))
                        Files.createDirectory(logDir);
                    else if(!Files.isDirectory(logDir))
                        JOptionPane.showMessageDialog(null, "ERROR " + logDir + 
                                " is not directory!", "MyLoggerError", 
                                JOptionPane.ERROR_MESSAGE);
                    
                    Files.write(Paths.get(LOG_FILE), lines, StandardOpenOption.CREATE);
                } catch (IOException ex)
                {
                    System.out.println(ex);
                    
                    // no need to react - it is allready to late
                }

            }
        }));
        
        // this will write all logs to console. files still will be created.
        MyLogger.addListener(new SoutLogListener());
    }
    
    // ----- Listener -----------------------------------------------
    
    public static void addListener(LogListener listener)
    {
        LISTENER.add(listener);
    }
    
    public static void removeListener(LogListener listener)
    {
        LISTENER.remove(listener);
    }
    
    private static void callAllListeners(String logText)
    {
        for (LogListener logListener : LISTENER)
        {
            logListener.talk(logText);
        }
    }
    
    // ----- Logging -------------------------------------------------
    
    public static void error(String text)
    {
        Entry e = new Entry("[ERROR] " + text, new Date().getTime());
        LOG.add(e);
        
        callAllListeners(e.toString());
                
    }
    
    public static void error(String text, Exception ex)
    {
        StringBuilder sb = new StringBuilder("[ERROR] ");
        sb.append(text);
        sb.append("\n");
        sb.append(ex.getClass().getSimpleName());
        sb.append("\n");
        sb.append(ex.getMessage());
        sb.append("\n");
        
        StackTraceElement[] stackTrace = ex.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace)
        {
            sb.append("    ");
            sb.append(stackTraceElement.toString());
            sb.append("\n");
        }
        
        Entry e = new Entry( sb.toString(), new Date().getTime());
        LOG.add(e);
        
        callAllListeners(e.toString());
                
    }
    
    public static void warning(String text)
    {
        Entry e = new Entry("[WARNING] " + text, new Date().getTime());
        LOG.add(e);
        
        callAllListeners(e.toString());
                
    }
    
    public static void info(String text)
    {
        Entry e = new Entry("[INFO] " + text, new Date().getTime());
        LOG.add(e);
        
        callAllListeners(e.toString());
                
    }
    
    public static void debug(String text)
    {
        Entry e = new Entry("[DEBUG] " + text, new Date().getTime());
        LOG.add(e);
        
        callAllListeners(e.toString());
                
    }
}

class Entry
{
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss:S");
    
    private final long timestamp;
    private final String text;
    
    public Entry(String text, long timestamp)
    {
        this.text = text;
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString()
    {
        return String.format("[%s] %s", format.format(new Date(timestamp)), text);
    }
}

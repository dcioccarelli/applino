import javax.servlet.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class SwingSetApplino implements ServletContextListener
{
    static SwingSet2 instance = null;

    public SwingSetApplino()
    {
        System.out.println("SwingSetApplino constructor: " + this.toString());
    }

    public void contextInitialized(ServletContextEvent event)
    {
        System.out.println("SwingSetApplino contextInitialized: " + this.toString());
        ServletContext sc = event.getServletContext();

        try
        {
            JFrame frame = (JFrame) sc.getAttribute("frame");
            frame.getContentPane().setLayout(new BorderLayout());
            instance = new SwingSet2(frame);
            frame.getContentPane().add(instance, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent event)
    {
        ServletContext sc = event.getServletContext();
        instance.setApplet(null);
        instance = null;
        System.out.println("SwingSetApplino contextDestroyed: " + this.toString());
    }
}
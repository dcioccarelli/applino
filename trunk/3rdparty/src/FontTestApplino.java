import com.applino.ApplinoFrame;

import javax.servlet.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ResourceBundle;
import java.util.Locale;

public class FontTestApplino implements ServletContextListener
{
    public void contextInitialized(ServletContextEvent event)
    {
        System.out.println("FontTestApplino contextInitialized: " + this.toString());
        ServletContext sc = event.getServletContext();

        try
        {
            ApplinoFrame frame = (ApplinoFrame)sc.getAttribute("frame");
            Font2DTest f2dt = new Font2DTest(frame, true);
            frame.getContentPane().add(f2dt);
            frame.pack();
            frame.center();
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
        System.out.println("FontTestApplino contextDestroyed: " + this.toString());
    }
}
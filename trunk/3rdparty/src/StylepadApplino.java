import com.applino.ApplinoFrame;

import javax.servlet.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ResourceBundle;
import java.util.Locale;

public class StylepadApplino implements ServletContextListener
{
    static Notepad instance = null;

    public void contextInitialized(ServletContextEvent event)
    {
        System.out.println("StylepadApplino contextInitialized: " + this.toString());
        ServletContext sc = event.getServletContext();

        try
        {
            ApplinoFrame frame = (ApplinoFrame)sc.getAttribute("frame");
            // main method
            // instantiate this application
            init(frame);
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
        instance = null;
        System.out.println("StylepadApplino contextDestroyed: " + this.toString());
    }

    public void init(JFrame frame)
    {
        ResourceBundle resources = ResourceBundle.getBundle("resources.Stylepad", Locale.getDefault());
        frame.setTitle(resources.getString("Title"));
        frame.setBackground(Color.lightGray);
        frame.getContentPane().setLayout(new BorderLayout());
        Stylepad stylepad = new Stylepad();
        frame.getContentPane().add("Center", stylepad);
        frame.setJMenuBar(stylepad.createMenubar());
        frame.pack();
        frame.setSize(600, 480);
    }
}
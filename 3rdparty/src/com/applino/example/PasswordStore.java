package com.applino.example;

import com.applino.ApplinoFrame;
import java.io.PrintStream;
import javax.servlet.*;
import javax.swing.*;
import passwordstore.swingx.app.Application;
import passwordstore.ui.*;

public class PasswordStore implements ServletContextListener
{
    private PasswordStoreApplication instance;
    
    public void contextInitialized(ServletContextEvent event)
    {
        ServletContext sc = event.getServletContext();
        try
        {
            ApplinoFrame frame = (ApplinoFrame)sc.getAttribute("frame");
            instance = new PasswordStoreApplication();
            instance.init(frame);
            frame.center();
            frame.setVisible(true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent event)
    {
        ServletContext sc = event.getServletContext();
        instance.setInstance(null);
        this.instance = null;
        System.out.println("Context destroyed...");
    }


    private static class PasswordStoreApplication extends Application
    {
        private Controller controller;

        public void init(JFrame frame)
        {
             frame.setIconImage((new ImageIcon(getClass().getResource("resources/Lock128x128.png"))).getImage());
             this.controller = new Controller(frame);
             frame.setLocationRelativeTo(null);
        }

        public String getName()
        {
            return getResourceAsString("appName");
        }

        protected boolean canExit()
        {
            return false;
        }
    }
}
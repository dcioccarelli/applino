package com.applino.example;

import com.applino.ApplinoFrame;

import javax.servlet.*;
import java.util.Enumeration;
import java.util.TimerTask;

public class SimpleApplino implements ServletContextListener
{
    public void contextInitialized(ServletContextEvent event)
    {
        ServletContext sc = event.getServletContext();

        for (Enumeration e = sc.getInitParameterNames(); e.hasMoreElements();)
            System.out.println(e.nextElement());

        try
        {
            ApplinoFrame frame = (ApplinoFrame)sc.getAttribute("frame");

            // Center the window
            frame.setSize(300, 300);
            frame.center();
            frame.setVisible(true);
            System.out.println(sc.getServerInfo());

            /*
            Timer timer = new Timer();
            timer.schedule(new Peeper(), 0, 5000);
            */
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent event)
    {
        ServletContext sc = event.getServletContext();
        System.out.println("Context destroyed...");
    }

    static class Peeper extends TimerTask
    {
        public void run()
        {
            System.out.println("Peep!");
        }
    }
}

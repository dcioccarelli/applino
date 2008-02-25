package com.applino;

import java.util.*;
import org.mortbay.jetty.webapp.WebAppContext;

import java.awt.*;

public class ApplinoContext extends WebAppContext
{
    private MenuItem menuItem;
    private ApplinoFrame frame;
    
    public ApplinoContext()
    {
        super();
    }

    public MenuItem getMenuItem()
    {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem)
    {
        this.menuItem = menuItem;
    }

    /**
     * Method to stop a running Applino context. Is typically called from the
     * StartApplinoListner in the ApplinoDeployer and from the "Window Closed"
     * and "Exit" listeners in ApplinoFrame.
     *
     * @see com.applino.ApplinoDeployer.StartApplinoListner
     * @see com.applino.ApplinoFrame#windowCloseListener
     * @see com.applino.ApplinoFrame#exitListener
     */
    public void startApplino()
    {
        try
        {
            String name = this.getDisplayName();
            if (name == null) name = this.getContextPath().substring(1);
            this.frame = new ApplinoFrame(this);
            this.frame.setTitle(name);
            this.setAttribute("frame", this.frame);
            System.out.println("Started: " + name);
            if (menuItem != null)
                menuItem.setLabel("Stop " + name);
            super.start();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void stopApplino()
    {
        try
        {
            String name = this.getDisplayName();
            if (name == null) name = this.getContextPath().substring(1);
            System.out.println("Stopping: " + name + "...");
            if (menuItem != null)
                menuItem.setLabel("Start " + name);
            this.frame.setVisible(false);
            this.removeAttribute("frame");
            this.frame = null;
            super.stop();
            System.gc();
            System.out.println("Stopped: " + name);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public String getMessage()
    {
        return "Today is sunny!";
    }
}

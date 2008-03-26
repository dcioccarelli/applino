package com.applino;

import org.mortbay.jetty.webapp.WebAppContext;

import java.awt.*;

/**
 * An ApplinoContext is simply an extension of a Jetty WebAppContext which also
 * deals with updating the task tray icon when Applini are started and stopped.
 * In addition it frees any resources associated with an Applino when it is
 * stopped and creates an ApplinoFrame for the Applino to use for displaying
 * any graphical output.
 */
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

    /**
     * The ApplinoContext needs to have access to the corresponding MenuItem
     * so that it can change the displayed status whenever the applino is started
     * or stopped.
     * @param menuItem The menu item corresponding to this Applino context.
     */
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
            frame.setVisible(false);
            removeAttribute("frame");
            frame.setContext(null);
            frame = null;
            super.stop();
            System.gc();
            System.out.println("Stopped: " + name);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

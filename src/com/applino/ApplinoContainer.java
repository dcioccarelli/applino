package com.applino;

import org.mortbay.jetty.*;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.nio.SelectChannelConnector;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;

/**
 * The ApplinoContainer class is simply a container class which configures
 * Jetty and starts the container running in the task tray.
 */
public class ApplinoContainer
{
    static  ApplinoContainer    instance = new ApplinoContainer();
    private TrayIcon            trayIcon;
    private ApplinoDeployer     deployer;

    public static void main(String[] args) throws Exception
    {
        String applinoHome = System.getProperty("applino.home");        
        System.setProperty("jetty.home", applinoHome);        
        File sysout = new File(applinoHome + "/logs/sysout.log");
        PrintStream out = new PrintStream(sysout);
        System.setOut(out);

        File syserr = new File(applinoHome + "/logs/syserr.log");
        PrintStream err = new PrintStream(syserr);
        System.setErr(err);

        if (args.length == 0)
            instance.startSystemTray();
        else if (args[0].equals("start"))
            instance.startContainer();
        else if (args[0].equals("stop"))
            instance.stopContainer();
    }

    private void startSystemTray() throws Exception
    {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage(ApplinoContainer.class.getResource("res/tray.gif"));

        PopupMenu popup = new PopupMenu();
        popup.addSeparator();
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(exitListener);
        popup.add(exitItem);

        trayIcon = new TrayIcon(image, "Applino", popup);
        instance.startContainer(popup);

        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener(actionListener);
        tray.add(trayIcon);
    }

    private synchronized void startContainer() throws Exception
    {
        startContainer(null);
    }

    private synchronized void startContainer(PopupMenu menu) throws Exception
    {
        Server server = new Server();
        Connector connector = new SelectChannelConnector();
        connector.setPort(8080);
        server.addConnector(connector);

        HandlerCollection handlers = new HandlerCollection();
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        handlers.setHandlers(new Handler[]{contexts});
        server.setHandler(handlers);

        server.start();

        System.out.println("Starting up...");
        deployer = new ApplinoDeployer();
        if (menu != null)
            deployer.setPopupMenu(menu);
        deployer.setContexts(contexts);
        deployer.setScanInterval(5);
        server.addLifeCycle(deployer);
        System.out.println("Applino contaier initialised.");
    }

    private synchronized void stopContainer()
    {
        System.out.println("Stopping applini...");
        this.notify();
    }

    ActionListener actionListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            trayIcon.displayMessage("Applino",
                    "Right click on this icon for a list of installed applini.",
                    TrayIcon.MessageType.INFO);
        }
    };

    ActionListener exitListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("Exiting...");
            System.exit(0);
        }
    };
}

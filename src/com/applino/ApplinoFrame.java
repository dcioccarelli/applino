package com.applino;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ApplinoFrame extends JFrame
{
    ApplinoContext context;

    /**
     * Creates a new ApplinoFrame. Typically this happens in the startApplino method of
     * the ApplinoContext class.
     * @param context The Applino context which will house the Applino
     * @see com.applino.ApplinoContext
     */
    public ApplinoFrame(ApplinoContext context)
    {
        this.context = context;
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        JMenuBar menuBar = initMenuBar();
        this.setJMenuBar(menuBar);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.addWindowListener(windowCloseListener);
    }

    public ApplinoContext getContext()
    {
        return context;
    }

    public void setContext(ApplinoContext context)
    {
        this.context = context;
    }

    public JMenuBar initMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu();

        fileMenu.setText("File");
        JMenuItem jMenuFileExit = new JMenuItem();
        jMenuFileExit.setText("Exit");
        jMenuFileExit.addActionListener(exitListener);
        fileMenu.add(jMenuFileExit);
        menuBar.add(fileMenu);

        return menuBar;
    }

    public void center()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height)
            frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width)
            frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

        return;
    }

    // -------------- Local listener attributes ---------------

    WindowListener windowCloseListener = new WindowAdapter()
    {
        public void windowClosing(WindowEvent w)
        {
            context.stopApplino();
        }
    };

    ActionListener exitListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            context.stopApplino();
        }
    };
}

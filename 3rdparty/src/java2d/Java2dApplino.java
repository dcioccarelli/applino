package java2d;

import com.applino.ApplinoFrame;
import java2d.*;

import javax.servlet.*;
import javax.swing.*;
import java.awt.Component;
import java.awt.*;

/**
 * User: doci
 * Date: 20/02/2008
 * Time: 12:04:50
 */
public class Java2dApplino implements ServletContextListener
{
    public void contextInitialized(ServletContextEvent event)
    {
        ServletContext sc = event.getServletContext();

        try
        {
            ApplinoFrame frame = (ApplinoFrame) sc.getAttribute("frame");
            init(frame);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent event)
    {
        ServletContext sc = event.getServletContext();
        System.out.println("Context destroyed...");
    }

    private void init(ApplinoFrame frame)
    {
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JPanel progressPanel = new JPanel()
        {
            public Insets getInsets()
            {
                return new Insets(40, 30, 20, 30);
            }
        };
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));

        panel.add(Box.createGlue());
        panel.add(progressPanel);
        panel.add(Box.createGlue());

        progressPanel.add(Box.createGlue());

        Dimension d = new Dimension(400, 20);
        Java2Demo.progressLabel = new JLabel("Loading, please wait...");
        Java2Demo.progressLabel.setMaximumSize(d);
        progressPanel.add(Java2Demo.progressLabel);
        progressPanel.add(Box.createRigidArea(new Dimension(1, 20)));

        Java2Demo.progressBar = new JProgressBar();
        Java2Demo.progressBar.setStringPainted(true);
        Java2Demo.progressLabel.setLabelFor(Java2Demo.progressBar);
        Java2Demo.progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        Java2Demo.progressBar.setMaximumSize(d);
        Java2Demo.progressBar.setMinimum(0);
        Java2Demo.progressBar.setValue(0);
        progressPanel.add(Java2Demo.progressBar);
        progressPanel.add(Box.createGlue());
        progressPanel.add(Box.createGlue());

        Rectangle ab = frame.getContentPane().getBounds();
        panel.setPreferredSize(new Dimension(ab.width, ab.height));
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.validate();
        frame.pack();
        frame.setSize(600, 480);
        frame.center();
        frame.setVisible(true);
        
        Java2Demo demo = new Java2Demo();
        frame.getContentPane().remove(panel);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(demo, BorderLayout.CENTER);

        frame.validate();
        frame.repaint();
        frame.pack();
        frame.setSize(600, 480);
        frame.center();
        frame.setVisible(true);
    }
}
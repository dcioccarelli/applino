package com.applino.example;

import com.applino.ApplinoFrame;

import javax.servlet.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class SimpleText
        implements ActionListener, ServletContextListener
{
    JMenuItem savetext, gettext, cleardisplay;
    JLabel messlab1;
    JTextArea disptext;
    Painter panel1paint;

    public void contextInitialized(ServletContextEvent event)
    {
        ServletContext sc = event.getServletContext();

        try
        {
            ApplinoFrame frame = (ApplinoFrame) sc.getAttribute("frame");
            init(frame);
            frame.setTitle("Simple Text Editor");
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
        System.out.println("Context destroyed...");
    }

    private void init(JFrame frame)
    { //Begin Constructor
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);
        JMenu filemenu = new JMenu("File", false);
        menubar.add(filemenu);
        savetext = new JMenuItem("Save Text to File");
        gettext = new JMenuItem("Get Text from File");
        cleardisplay = new JMenuItem("Clear Display");
        filemenu.add(savetext);
        filemenu.add(gettext);
        filemenu.add(cleardisplay);

        frame.getContentPane().setLayout(new GridLayout(1, 2));
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1.setBackground(Color.white);
        frame.getContentPane().add(panel1);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());
        panel2.setBackground(Color.white);
        frame.getContentPane().add(panel2);

        //Create message area from color and text string
        panel1paint = new Painter(Color.black, "Ready");
        Dimension dimension = new Dimension();
        dimension.setSize(200, 25);
        panel1paint.setSize(dimension);
        //Add message area to Panel 1
        panel1.add(BorderLayout.CENTER, panel1paint);

        //Add message label to Panel 1
        messlab1 = new JLabel();
        panel1.add(BorderLayout.SOUTH, messlab1);

        //Create text area for panel 2
        disptext = new JTextArea();
        disptext.setFont(new Font("Serif",
                Font.ITALIC, 12));
        disptext.setLineWrap(true);
        disptext.setWrapStyleWord(true);
        disptext.setEditable(true);
        JScrollPane areaScrollPane = new
                JScrollPane(disptext);
        areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(
                new Dimension(200, 175));
        areaScrollPane.setBorder(
                BorderFactory.createTitledBorder("Enter or Edit Text:"));
        panel2.add(areaScrollPane);

        //This class listens for menu item events
        savetext.addActionListener(this);
        gettext.addActionListener(this);
        cleardisplay.addActionListener(this);
    } //End Constructor

    protected void clearDisplay()
    {
        messlab1.setText("");
        panel1paint.setColor(Color.black);
        panel1paint.setText("Ready");
        panel1paint.repaint();
        disptext.setText("");
    }

    public void actionPerformed(ActionEvent event)
    {
        Object source = event.getSource();
        String returned = null;
        FileInputStream in = null;
        FileOutputStream out = null;

        if (source == cleardisplay)
        {
            clearDisplay();
        }

        if (source == savetext)
        {
            returned = disptext.getText();
            if (returned != null)
            {
                //Write to file
                try
                {
                    byte b[] = returned.getBytes();
                    String outputFileName =
                            System.getProperty("user.home",
                                    File.separatorChar + "home" +
                                            File.separatorChar + "zelda") +
                                    File.separatorChar + "text.txt";
                    out = new FileOutputStream(outputFileName);
                    out.write(b);
                    panel1paint.setText(
                            " Text successfully saved.");
                    panel1paint.setColor(Color.blue);
                    panel1paint.repaint();
                    disptext.setText(returned);
                }
                catch (java.io.IOException e)
                {
                    messlab1.setText("Cannot write to text.txt");
                }
                finally
                {
                    if (out != null)
                    {
                        try
                        {
                            out.close();
                        }
                        catch (java.io.IOException e)
                        {
                            messlab1.setText("Cannot close file");
                        }
                    }
                }
            }
            else
            {
                messlab1.setText("No Text to Save");
            }
        }

        if (source == gettext)
        {
            //Read from file
            try
            {
                String inputFileName = System.getProperty(
                        "user.home",
                        File.separatorChar + "home" +
                                File.separatorChar + "zelda") +
                        File.separatorChar + "text.txt";
                File inputFile = new File(inputFileName);
                in = new FileInputStream(inputFile);
                byte bt[] = new byte[(int) inputFile.length()];
                in.read(bt);
                String s = new String(bt);
                disptext.setText(s);
                panel1paint.setText(" Text read from file: ");
                panel1paint.setColor(Color.blue);
                panel1paint.repaint();
            }
            catch (java.io.IOException e)
            {
                messlab1.setText("Cannot read from text.txt");
            }
            finally
            {
                if (in != null)
                {
                    try
                    {
                        in.close();
                    }
                    catch (java.io.IOException e)
                    {
                        messlab1.setText("Cannot close file");
                    }
                }
            }
        }
    }
}

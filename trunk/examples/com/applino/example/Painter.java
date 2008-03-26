package com.applino.example;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

public class Painter extends JPanel
{
    Color c;
    String s;

    Painter(Color c, String s)
    {
        this.s = s;
        this.c = c;
    }

    protected void setColor(Color c)
    {
        this.c = c;
    }

    protected Color getColor()
    {
        return this.c;
    }

    protected void setText(String s)
    {
        this.s = s;
    }

    protected String getText()
    {
        return this.s;
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2;
        g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        //Make background white
        g2.setColor(Color.white);
        g2.fillRect(0, 0, getSize().width - 1, getSize().height - 1);
        //Set font rendering context and font
        FontRenderContext frc = g2.getFontRenderContext();
        Font f = new Font("Helvetica", Font.PLAIN, 15);
        //Create styled text from font and string
        TextLayout tl = new TextLayout(s, f, frc);
        //Get the size of the drawing area
        Dimension theSize = getSize();
        //Set the 2D graphics context color for drawing the text
        g2.setColor(c);
        //Draw the text into the drawing area
        tl.draw(g2, theSize.width / 30, theSize.height / 2);
        //Put a blue box around the styled text
        //unless the text string is "Ready"
        if (this.s != "Ready")
        {
            g2.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
        }
    }
}

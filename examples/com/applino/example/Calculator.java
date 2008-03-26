package com.applino.example;

import com.applino.ApplinoFrame;

import javax.servlet.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

/**
 * Calculator example application.
 */
public class Calculator implements ActionListener, ServletContextListener
{
    // components
    // these variables are made global
    // so they can be referenced in more than one method
    // text fields for operands
    JTextField operandOneField;
    JTextField operandTwoField;
    // text field for result
    JTextField resultField;
    // buttons to initiate calculations
    JButton addButton;
    JButton subtractButton;
    JButton multiplyButton;
    JButton divideButton;
    // source of ActionEvent
    Object source;

    public Calculator()
    {
        System.out.println("Calculator instance created...");
    }

    public void contextInitialized(ServletContextEvent event)
    {
        ServletContext sc = event.getServletContext();

        try
        {
            System.out.println("Calculator context initialised...");
            ApplinoFrame frame = (ApplinoFrame)sc.getAttribute("frame");
            // main method
            // instantiate this application
            init(frame);
            frame.setSize(400, 240);
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
        System.out.println("Calculator context destroyed...");
    }

    public void init(JFrame frame)
    {
        // constructor
        // call the constructor of this application's superclass
        // panels for organizing text fields and button
        JPanel operandPanel;
        JPanel buttonPanel;
        JPanel resultPanel;
        // the content pane
        Container cp;
        // get this application's content pane
        cp = frame.getContentPane();
        // use a grid layout with three rows and one column
        cp.setLayout(new GridLayout(3, 1));

        // instantiate the panel to hold the operand text fields
        operandPanel = new JPanel();
        operandPanel.setLayout(new FlowLayout());
        // instantiate and add the text fields to the panel
        operandOneField = new JTextField("0.00", 12);
        operandOneField.setHorizontalAlignment(SwingConstants.RIGHT);
        operandTwoField = new JTextField("0.00", 12);
        operandTwoField.setHorizontalAlignment(SwingConstants.RIGHT);
        operandPanel.add(operandOneField);
        operandPanel.add(operandTwoField);
        // add the panel to the content pane
        cp.add(operandPanel);

        // instantiate the panel to hold the button
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        // instantiate and add the buttons to the panel
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        buttonPanel.add(addButton);
        subtractButton = new JButton("Subtract");
        subtractButton.addActionListener(this);
        buttonPanel.add(subtractButton);
        multiplyButton = new JButton("Multiply");
        multiplyButton.addActionListener(this);
        buttonPanel.add(multiplyButton);
        divideButton = new JButton("Divide");
        divideButton.addActionListener(this);
        buttonPanel.add(divideButton);
        // add the panel to the content pane
        cp.add(buttonPanel);

        // instantiate the panel to hold the result text field
        resultPanel = new JPanel();
        resultPanel.setLayout(new FlowLayout());
        // instantiate and add the text field to the panel
        resultField = new JTextField(20);
        resultField.setHorizontalAlignment(SwingConstants.RIGHT);
        resultField.setEditable(false);
        resultField.setBackground(Color.white);
        resultPanel.add(resultField);
        // add the panel to the content pane
        cp.add(resultPanel);
    }

    public void showResult()
    {
        // perform the arithmetic and show the result
        double operandOne = 0.0;
        double operandTwo = 0.0;
        boolean validOperands;
        DecimalFormat df = new DecimalFormat("0.00");
        // try to get valid doubles from the operand text fields
        try
        {
            operandOne = Double.parseDouble(operandOneField.getText());
            operandTwo = Double.parseDouble(operandTwoField.getText());
            validOperands = true;
        }
        catch (NumberFormatException nfe)
        {
            validOperands = false;
        }
        // peform calculation if operands are valid doubles
        if (validOperands)
        {
            if (source == addButton)
                resultField.setText(df.format(operandOne + operandTwo));
            else if (source == subtractButton)
                resultField.setText(df.format(operandOne - operandTwo));
            else if (source == multiplyButton)
                resultField.setText(df.format(operandOne * operandTwo));
            else if (source == divideButton)
            {
                if (operandTwo != 0.0)
                    resultField.setText(df.format(operandOne / operandTwo));
                else
                    resultField.setText("Attempt to divide by zero");
            }
        }
        else
        {
            resultField.setText("Invalid operand");
        }
    }

    public void actionPerformed(ActionEvent ae)
    {
        // respond to button clicks
        // find out which button was pressed
        source = ae.getSource();
        // show the result
        showResult();
    }

}

package com.nulldev.phouse.desktop;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.BoxLayout;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingConstants;

import com.nulldev.lib.LibNullWIFISocketServer;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WIFIServer {

	private JFrame frmPhouseDesktop;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WIFIServer window = new WIFIServer();
					window.frmPhouseDesktop.setVisible(true);
					
					//Stop the server if the window is closed...
					window.frmPhouseDesktop.addWindowListener(new WindowAdapter()
					{
					    public void windowClosing(WindowEvent e)
					    {
					    	LibNullWIFISocketServer.stopServer();
					    }
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WIFIServer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPhouseDesktop = new JFrame();
		frmPhouseDesktop.setTitle("Phouse Desktop - WIFI");
		frmPhouseDesktop.setBounds(100, 100, 450, 300);
		frmPhouseDesktop.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmPhouseDesktop.getContentPane().add(panel, BorderLayout.NORTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblRunning = new JLabel("Phouse is now running!");
		lblRunning.setHorizontalAlignment(SwingConstants.CENTER);
		lblRunning.setFont(new Font("Dialog", Font.BOLD, 20));
		GridBagConstraints gbc_lblRunning = new GridBagConstraints();
		gbc_lblRunning.insets = new Insets(0, 0, 5, 0);
		gbc_lblRunning.gridx = 0;
		gbc_lblRunning.gridy = 0;
		panel.add(lblRunning, gbc_lblRunning);
		
		JLabel lblDesc = new JLabel("Please enter the IP below on your phone:");
		lblDesc.setHorizontalAlignment(SwingConstants.CENTER);
		lblDesc.setFont(new Font("Dialog", Font.PLAIN, 12));
		GridBagConstraints gbc_lblDesc = new GridBagConstraints();
		gbc_lblDesc.gridx = 0;
		gbc_lblDesc.gridy = 1;
		panel.add(lblDesc, gbc_lblDesc);
		
		final JLabel lblIP = new JLabel("IP: " + LibNullWIFISocketServer.serverIP);
		frmPhouseDesktop.getContentPane().add(lblIP, BorderLayout.CENTER);
		
		JPanel panelButtom = new JPanel();
		frmPhouseDesktop.getContentPane().add(panelButtom, BorderLayout.SOUTH);
		
		JButton btnChangeIp = new JButton("Change IP");
		btnChangeIp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//User changed the IP...
				String newIP = JOptionPane.showInputDialog("Enter the new IP:");
				LibNullWIFISocketServer.stopServer();
				LibNullWIFISocketServer.serverIP = newIP;
				
				//Update labels
				lblIP.setText("IP: " + LibNullWIFISocketServer.serverIP);
				
				//Start the server
				LibNullWIFISocketServer.startServer();
			}
		});
		panelButtom.add(btnChangeIp);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LibNullWIFISocketServer.stopServer();
				System.exit(0);
			}
		});
		panelButtom.add(btnQuit);
	}
}

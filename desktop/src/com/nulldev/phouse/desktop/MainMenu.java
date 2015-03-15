package com.nulldev.phouse.desktop;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;

import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JButton;

import com.nulldev.lib.LibNullSensorTranslater;
import com.nulldev.lib.LibNullWIFISocketServer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainMenu {

	private JFrame frmPhouseDesktop;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu window = new MainMenu();
					window.frmPhouseDesktop.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainMenu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPhouseDesktop = new JFrame();
		frmPhouseDesktop.setTitle("Phouse Desktop");
		frmPhouseDesktop.setBounds(100, 100, 410, 80);
		frmPhouseDesktop.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblSelectACommunication = new JLabel("Select a communication mode:");
		lblSelectACommunication.setFont(new Font("Dialog", Font.BOLD, 20));
		lblSelectACommunication.setHorizontalAlignment(SwingConstants.CENTER);
		frmPhouseDesktop.getContentPane().add(lblSelectACommunication, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		frmPhouseDesktop.getContentPane().add(panel, BorderLayout.CENTER);
		
		JButton btnWifi = new JButton("WIFI");
		btnWifi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmPhouseDesktop.dispose();
				LibNullWIFISocketServer.startServer();
				LibNullSensorTranslater.init();
				WIFIServer.main(null);
			}
		});
		panel.add(btnWifi);
		
		JButton btnUsb = new JButton("USB");
		panel.add(btnUsb);
		
		JButton btnBluetooth = new JButton("Bluetooth");
		panel.add(btnBluetooth);
	}
}

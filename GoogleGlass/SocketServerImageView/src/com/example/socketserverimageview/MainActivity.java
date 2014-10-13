package com.example.socketserverimageview;


import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import com.example.socketserverimageview.R;

import android.os.Bundle;
import android.os.PowerManager;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * FOR ANDROID PHONE
 */
public class MainActivity extends Activity {
	
	private static int BACKGROUND_COLOR_ACTIVE = Color.rgb(100, 100, 50);
	private static int BACKGROUND_COLOR_INACTIVE = Color.rgb(51, 51, 80);
	public static String PACKAGE_NAME;

	private TextView info, infoip, instruction, status;
	private ImageView imageDisplay;
	private PowerManager.WakeLock wakeLock;
	
	private ServerSocket serverSocket;
	private Thread socketServerThread;
	private boolean serverThreadRunning;

	/*
	 * Set UIs
	 * Start socket server thread
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PACKAGE_NAME = getPackageName();
		setContentView(R.layout.activity_main);
		
		status = (TextView) findViewById(R.id.status);
		info = (TextView) findViewById(R.id.info);
		infoip = (TextView) findViewById(R.id.infoip);
		infoip.setText(getIpAddress());
		instruction = (TextView) findViewById(R.id.instruction);
		instruction.setText("There may be some delay depending on wifi.\n"
				+ "Try toggling gestures on Glass if the casted image does not match the screen displayed on Glass.\n"
				+ "If casted image continues to be frozen, please restart casting.\n");
		
		imageDisplay = (ImageView) findViewById(R.id.image);
		imageDisplay.setBackgroundColor(BACKGROUND_COLOR_INACTIVE);
		imageDisplay.setImageResource(R.drawable.ready);
		
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, PACKAGE_NAME);

		socketServerThread = new Thread(new SocketServerThread());
		socketServerThread.start();
	}

	/*
	 * Make sure to close the server socket before quitting
	 */
	@Override
	protected void onDestroy() {
		serverThreadRunning = false;
		
		if (serverSocket != null && !serverSocket.isClosed()) {
			try {
				serverSocket.close();
				status.setText("Connection ended. Restart the app for the new session");
				
				Log.d(PACKAGE_NAME, "Server socket closed");
			} catch (IOException e) {
				Log.e(PACKAGE_NAME, "OnDestroy IOException: "+e.toString());
			}
		}
		
		super.onDestroy();
	}
	
	/*
	 * Acquire wake lock when app comes into focus
	 */
	@Override
	protected void onResume() {
		if (!wakeLock.isHeld()) {
			wakeLock.acquire();
			Log.d(PACKAGE_NAME, "Acquire wake lock");
		}
		
		super.onResume();
	}
	
	/*
	 * Release wake lock when app goes to sleep
	 */
	@Override
	protected void onPause() {
		if (wakeLock.isHeld()) {
			wakeLock.release();
			Log.d(PACKAGE_NAME, "Release wake lock");
		}
		
		super.onPause();
	}
	
	
	/**
	 * Inner class
	 * Thread to handle network connections
	 */
	private class SocketServerThread extends Thread {
		
		static final int SocketServerPORT = 8080;
		
		Socket socket = null;
		DataInputStream dataInputStream = null;
		boolean connectionAvailable = false;
		
		/*
		 * Setup server socket
		 */
		public boolean setupConnection() {
			try {
				serverSocket = new ServerSocket(SocketServerPORT);
				
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						info.setText("I'm waiting here: " + serverSocket.getLocalPort());
					}
				});
				
				Log.d(PACKAGE_NAME, "Server socket open");
				return true;
			} catch (IOException e) {
				Log.e(PACKAGE_NAME, "setupConnection IOException: "+e.toString());
			}
			return false;
		}
		
		/*
		 * Open socket and data input stream
		 */
		public boolean openConnection() {
			// Must pass two separate socket checks
			if (serverSocket != null && !serverSocket.isClosed()) {
				try {
					socket = serverSocket.accept();
					Log.d(PACKAGE_NAME, "Socket open");
				} catch (IOException e) {
					if (serverThreadRunning) {
						Log.e(PACKAGE_NAME, "openConnection server socket IOException: "+e.toString());
					}
					else {
						// Server connection is configured to close-reopen if client closes connection
						// As the result, when user quits the app,
						// one extra attempt is made to open socket connection before app terminates
						// This is because of the timing between server socket closing and thread running
						// so do nothing
						/*Log.d(PACKAGE_NAME, "openConnection server socket IOException: "+e.toString()
								+ "; Server socket closed as app terminates");*/
					}
				}
			}
			else {
				socket = null;
			}
				
			if (socket != null && !socket.isClosed()) {
				try {
					dataInputStream = new DataInputStream(socket.getInputStream());
					
					Log.d(PACKAGE_NAME, "Input stream open");
					return true;
				} catch (IOException e) {
					Log.e(PACKAGE_NAME, "openConnection socket IOException: "+e.toString());
				}
			}
			
			return false;
		}
		
		/*
		 * Close connection
		 */
		public void closeConnection() {
			connectionAvailable = false;

			if (socket != null && !socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e) {
					Log.e(PACKAGE_NAME, "closeConnection socket IOException: "+e.toString());
				}
			}
			if (dataInputStream != null) {
				try {
					dataInputStream.close();
				} catch (IOException e) {
					Log.e(PACKAGE_NAME, "closeConnection dataInputStream IOException: "+e.toString());
				}
			}
			
			MainActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					imageDisplay.setBackgroundColor(BACKGROUND_COLOR_INACTIVE);
					imageDisplay.setImageResource(R.drawable.ready);
					status.setText("Connection Ended");
					imageDisplay.invalidate();
				}
			});
			
			Log.d(PACKAGE_NAME, "Socket and input stream closed");
		}
		
		/*
		 * Maintain network connection
		 * Display image on phone screen
		 */
		@Override
		public void run() {
			try {
				serverThreadRunning = true;
				boolean setupSuccess = setupConnection();
				
				// Prevent server from closing after client closes
				while (setupSuccess && serverThreadRunning) {
					try {
						connectionAvailable = openConnection();
						
						while (dataInputStream != null && connectionAvailable && !interrupted()) {
							// If no data sent from client, this code will block the program
							int len = dataInputStream.readInt();
							Log.d(PACKAGE_NAME, "byte array size: "+len);
							
							// Byte array to bitmap
							byte[] screenshotByteArray = new byte[len];
							dataInputStream.readFully(screenshotByteArray);
							
							//**Log
							/*String bytes = "";
							for (int i = 0; i < 30; i++) {
								bytes += (" " +screenshotByteArray[i]);
							}
							bytes += "\n";
							for (int i = 0; i < 30; i++) {
								bytes += (" " +screenshotByteArray[len-31+i]);
							}
							Log.d(PACKAGE_NAME, bytes);*/
							
							final Bitmap bitmap = BitmapFactory.decodeByteArray(screenshotByteArray, 0, len);
							//**Log
							/*if (bitmap == null) {
								Log.d(PACKAGE_NAME, "bitmap is null");
							}
							else {
								Log.d(PACKAGE_NAME, "bitmap is not null");
							}*/
							
							// Bitmap to picture
							MainActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									imageDisplay.setBackgroundColor(BACKGROUND_COLOR_ACTIVE);
									imageDisplay.setImageBitmap(bitmap);
									status.setText("Casting...");
									imageDisplay.invalidate();
								}
							});
						} //inner while, for single connection
					} catch (IOException e) {
						Log.d(PACKAGE_NAME, "Single Socket IOException: "+e.toString());
							
						closeConnection();
					}
				} //outer while, for consecutive connections
			} finally {
				closeConnection();
				
				if (serverSocket != null && !serverSocket.isClosed()) {
					try {
						serverSocket.close();	
						status.setText("Connection closed. Restart the app for the new connection");
						Log.d(PACKAGE_NAME, "Server socket closed");
					} catch (IOException e) {
						Log.e(PACKAGE_NAME, "Server Socket IOException: "+e.toString());
					}
				}
			} //finally
		}
	} //Inner class: thread

	
	/*
	 * Fetch the device's ip address
	 */
	private String getIpAddress() {
		String ip = "";
		
		try {
			Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
			
			while (enumNetworkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
				Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();
				
				while (enumInetAddress.hasMoreElements()) {
					InetAddress inetAddress = enumInetAddress.nextElement();

					if (inetAddress.isSiteLocalAddress()) {
						ip += "SiteLocalAddress: " + inetAddress.getHostAddress() + "\n";
					}
				}
			}

		} catch (SocketException e) {
			Log.e(PACKAGE_NAME, "ip IOException: "+e.toString());
			ip += "Something Wrong! " + e.toString() + "\n";
		}

		return ip;
	}
}

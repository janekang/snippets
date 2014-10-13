package com.example.socketclientimageview;

import android.os.Handler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


/**
 * Handler for network connection
 */
public class CastingNetworkHandler extends Handler {	
	public static String SCREENSHOT = "screenshot";
	
	private Socket socket;
	private DataOutputStream dataOutputStream;
	private boolean castingEnabled;
	
	/*
	 * Set up looper
	 */
	public CastingNetworkHandler(Looper looper) {
		super(looper);
		castingEnabled = false;
		socket = null;
		dataOutputStream = null;
	}

	/*
	 * On receiving a message from Handler (from BaseCastingNetworkActivity),
	 * respond accordingly depending on message.what tag
	 */
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what)
		{
			case BaseCastingNetworkActivity.START_CONNECTION:
				// Fetch ip address and port
				Bundle bundle = msg.getData();
				String data = bundle.getString("dst");
				String dstAddress = data.substring(0, data.indexOf(":"));
				int dstPort = Integer.parseInt(data.substring(data.indexOf(":")+1));
				
				Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, "IP: " + dstAddress + " Port: "+ dstPort);
				
				// Wrap up past connection
				if (socket != null) {
					try {
						socket.close();
						socket = null;
					} catch (IOException e) {
						Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, "IOException: " + e.toString());
					}
				}
				if (dataOutputStream != null) {
					try {
						dataOutputStream.close();
						dataOutputStream = null;
					} catch (IOException e) {
						Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, "IOException: " + e.toString());
					}
				}
				
				// Open new connection
				try {
					socket = new Socket(dstAddress, dstPort);
					dataOutputStream = new DataOutputStream(socket.getOutputStream());
					castingEnabled = true;
				} catch (UnknownHostException e) {
					e.printStackTrace();
					Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, "UnknownHostException: " + e.toString());
					castingEnabled = false;
				} catch (IOException e) {
					e.printStackTrace();
					Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, "IOException: " + e.toString());
					castingEnabled = false;
				}
				break;
				
				
			case BaseCastingNetworkActivity.SEND_SCREENSHOT:
				byte[] byteArray = msg.getData().getByteArray(SCREENSHOT);	
				int len = byteArray.length;
				
				// **LOG FOR DEBUG
				/*if (len == 0){
					Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, "Received byte array size 0");
				}
				else {
					String bytes = "";
					for (int i = 0; i < 30; i++) {
						bytes += (" " + byteArray[i]);
					}
					bytes += "\n";
					for (int i = 0; i < 30; i++) {
						bytes += (" " + byteArray[byteArray.length-31+i]);
					}
					Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, bytes);
				}*/
				
				if (len > 0){
					try {
						dataOutputStream.writeInt(len);
						dataOutputStream.write(byteArray, 0, len);
						Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, "Screenshot data sent from socket");
					} catch (IOException e) {
						Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, "IOException: " + e.toString());
					}
				}
				else {
					Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, "Error: Socket found screenshot byte array size <= 0");
				}
				break;
				
				
			case BaseCastingNetworkActivity.QUIT_CONNECTION:
				if (socket != null) {
					try {
						socket.close();
						socket = null;
					} catch (IOException e) {
						Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, "IOException: " + e.toString());
					}
				}
				if (dataOutputStream != null) {
					try {
						dataOutputStream.close();
						dataOutputStream = null;
					} catch (IOException e) {
						Log.d(BaseCastingNetworkActivity.PACKAGE_NAME, "IOException: " + e.toString());
					}
				}
				
				castingEnabled = false;
				break;
		}
	}
	
	public boolean isCasting() {
		return castingEnabled;
	}
}



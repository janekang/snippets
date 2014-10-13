package com.example.socketclientimageview;

import android.os.HandlerThread;

/**
 * Background thread for connection
 */
public class CastingNetworkHandlerThread extends HandlerThread {
	public CastingNetworkHandlerThread() {
		super("CastingNetworkHandlerThread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
	}
}

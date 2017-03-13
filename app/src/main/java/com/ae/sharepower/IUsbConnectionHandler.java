package com.ae.sharepower;
public interface IUsbConnectionHandler {
	void onUsbStopped();
	void onErrorLooperRunningAlready();
	void onDeviceNotFound();
}

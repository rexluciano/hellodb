package com.rex.worker;

import android.os.Binder;

public class WorkerBinder extends Binder {
	Worker getService() {
		return new Worker();
	}
}
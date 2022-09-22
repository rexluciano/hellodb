package com.rex.worker;

public abstract class ThreadWorker {
	public abstract void onWork();

	public abstract void onProgress();

	public abstract void onDone();
}
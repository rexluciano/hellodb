package com.rex.worker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.content.Intent;

import android.app.Service;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.app.Notification;

import com.rex.worker.R;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Worker extends Service {

	private Context context;
	private Handler handler;
	private Message message;
	private ThreadWorker worker;
	private Bundle bundle;
	private IBinder mWorkerBinder = new WorkerBinder();
	private String str = "Starting...";
	private NotificationManager manager;
	private Notification n;

	private BroadcastReceiver mActionReceiver;
	private BroadcastReceiver mWorkReceiver;
	private BroadcastReceiver mLowMemoryReceiver;

	private ExecutorService executorService;

	private Runnable workRunnabble = new Runnable() {
		@Override
		public void run() {
			if (worker != null) {
				worker.onDone();
			}
		}
	};

	public Worker() {
		super();
		onCreate();
	}

	public Worker create(Context context) {
		this.context = context;
		initializeLogic();
		return this;
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		super.onTaskRemoved(rootIntent);
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (manager != null) {
			manager.cancel(0);
		}
	}

	@Override
	public ComponentName startService(Intent service) {
		Intent i = service;
		if (i.getAction().equals("com.rex.worker.action.START")) {
			if (workRunnabble != null) {
				workRunnabble.run();
				setMessage("Worker is starting.");
			}
		}
		return super.startService(service);
	}

	@Override
	public boolean stopService(Intent name) {
		return super.stopService(name);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		bundle.putString("message", "Low Memory Warning!");
		Intent intent = new Intent();
		intent.setAction("com.rex.worker.action.LOW_MEMORY_WARNING");
		intent.putExtras(bundle);
		if (context != null) {
			context.sendBroadcast(intent);
		}
		if (executorService != null) {
			executorService.shutdown();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mWorkerBinder;
	}

	private void initializeLogic() {
		this.executorService = Executors.newSingleThreadExecutor();
		manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		long now = SystemClock.uptimeMillis();
		long next = now + (1000 - now % 1000);
		handler = new Handler(Looper.getMainLooper());
		if (workRunnabble != null) {
			workRunnabble.run();
			setMessage("Worker is running.");
			if (worker != null) {
				worker.onProgress();
			}
		}
	}

	public Worker work(ThreadWorker worker) {
		this.worker = worker;
		return this;
	}

	public void start() {
		if (context != null) {
			Intent i = new Intent(context, Worker.class);
			context.startService(i);
			startBackground();
		} else {
			throw new NullPointerException("Context must not null.");
		}
	}

	public void stop() {
		if (context != null) {
			Intent i = new Intent(context, Worker.class);
			context.stopService(i);
		} else {
			throw new NullPointerException("Context must not null.");
		}
	}

	public boolean isStop() {
		return executorService.isShutdown();
	}

	private void cancel() {
		if (handler != null) {
			handler.removeCallbacks(workRunnabble);
		}
	}

	private void setMessage(String str) {
		this.str = str;
	}

	public Worker doNotification() {
		showNotification("Worker started.", true);
		mActionReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals("com.rex.worker.action.STOP")) {
					Intent i = new Intent(context, Worker.class);
					context.stopService(i);
					setMessage("Worker is stopping.");
					manager.cancel(0);
				}
			}
		};

		context.registerReceiver(mActionReceiver, new IntentFilter("com.rex.worker.action.STOP"));

		mWorkReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals("com.rex.worker.action.START")) {
					setMessage("Thread worker is running.");
					showNotification("Thread worker is running.", true);
				}
			}
		};

		context.registerReceiver(mWorkReceiver, new IntentFilter("com.rex.worker.action.START"));

		mLowMemoryReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals("com.rex.worker.action.LOW_MEMORY_WARNING")) {
					setMessage("Low Memory Warning.");
					showNotification("Low Memory Warning.", false);
				}
			}
		};

		context.registerReceiver(mLowMemoryReceiver, new IntentFilter("com.rex.worker.action.LOW_MEMORY_WARNING"));
		return this;
	}

	private void showNotification(String message, boolean actionShowed) {
		if (context != null) {
			createNotificationChannel();
			Notification.Builder mNotification = new Notification.Builder(context, "Worker");
			mNotification.setSmallIcon(R.drawable.cmd_cube_outline);
			mNotification.setContentTitle("Worker Service");
			mNotification.setContentText(message);
			mNotification.setChannelId("Worker");
			mNotification.setAutoCancel(false);

			Intent intent = new Intent();
			intent.setAction("com.rex.worker.action.STOP");
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
					PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);

			Notification.Action action = new Notification.Action.Builder(
					Icon.createWithResource(context, R.drawable.cmd_window_close), "Stop Worker", pendingIntent)
							.build();

			if (actionShowed) {
				mNotification.addAction(action);
			}

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				n = mNotification.build();
			} else {
				n = mNotification.getNotification();
			}

			n.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

			manager.notify(0, n);
		}
	}

	private void createNotificationChannel() {
		CharSequence name = "Worker";
		String description = "Worker service.";
		int importance = NotificationManager.IMPORTANCE_LOW;
		NotificationChannel channel = new NotificationChannel("Worker", name, importance);
		channel.setDescription(description);
		NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
		notificationManager.createNotificationChannel(channel);

	}

	private void startBackground() {
		if (worker != null) {
			worker.onProgress();
		}
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				if (worker != null) {
					worker.onWork();
				}
				handler.post(workRunnabble);
			}
		});
	}

}
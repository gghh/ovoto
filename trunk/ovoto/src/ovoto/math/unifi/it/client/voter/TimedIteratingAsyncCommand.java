package ovoto.math.unifi.it.client.voter;

import java.util.Iterator;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class TimedIteratingAsyncCommand<T,S> implements AsyncCallback<S> {


	public abstract void execute(T t, AsyncCallback<S> callback);
	public abstract void onComplete();



	//Iterable<T> items;
	private Iterator<T> iterator;
	private AsyncCallback<S> callback;
	private int delay;
	
	//private AsyncCommand<T,S> command;
	private Timer timer;
	public TimedIteratingAsyncCommand() {
		//this.items = items;
		//this.iterator = items.iterator();
		//this.command = command;
		//this.callback = callback;

		timer = new Timer() {
			@Override
			public void run() {
				final T item = iterator.next();
				TimedIteratingAsyncCommand.this.execute(item, TimedIteratingAsyncCommand.this);
			}
		};
	}

	@Override
	public void onSuccess(S result) {
		callback.onSuccess(result);
		if(iterator.hasNext()) 
			timer.schedule(delay);//schedule next
		else
			onComplete();
	}

	@Override
	public void onFailure(Throwable caught) {
		callback.onFailure(caught);
		if(iterator.hasNext()) 
			timer.schedule(10*delay);//schedule next
		else
			onComplete();
	}


	public void run(Iterable<T> items, AsyncCallback<S> c, int delayInMilliseconds) {
		delay = delayInMilliseconds;
		iterator = items.iterator();
		callback = c;
		if(iterator.hasNext())
			timer.run();
		else
			onComplete();
	}

}
package analytcs;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.lojapro.can.R;

import android.app.Application;
import android.content.Context;

/**
 * This is a subclass of {@link Application} used to provide shared objects for this app, such as
 * the {@link Tracker}.
 */
public class MyTrack extends Application {
  private Tracker mTracker;

  /**
   * Gets the default {@link Tracker} for this {@link Application}.
   * @return tracker
   */
  synchronized public Tracker getDefaultTracker() {
    if (mTracker == null) {
      GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
      // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
      mTracker = analytics.newTracker(R.xml.global_tracker);
    }
    return mTracker;
  }
}
  
/*
public class MyTrack {
	Context ctx;
	Tracker t;
	private GoogleAnalytics analytics = null;
	
	
	public MyTrack(Context ctx2) {
		this.ctx = ctx2;
		analytics = GoogleAnalytics.getInstance(ctx);
	}
	public Tracker getTracker(){
		this.t = analytics.newTracker("UA-39834712-2");
		return t;
		
	}
}
*/
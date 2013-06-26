package www.funsumer.net;

import www.funsumer.net.widget.ExtendedImageDownloader;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;

public class ALL extends Application {

	private int state;
	private int delete12;
    private static Context context;

	@Override
	public void onCreate() {
		// �꾩뿭 蹂�닔 珥덇린��
		state = 0;
		super.onCreate();
        ALL.context = getApplicationContext();
     // This configuration tuning is custom. You can tune every option, you may tune some of them, 
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.memoryCacheSize(2 * 1024 * 1024) // 2 Mb
			.denyCacheImageMultipleSizesInMemory()
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			.imageDownloader(new ExtendedImageDownloader(getApplicationContext()))
			.tasksProcessingOrder(QueueProcessingType.LIFO)
			.enableLogging() // Not necessary in common
			.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

    public static Context getAppContext() {
        return ALL.context;
    }
   /* public class UILApplication extends Application {
    	@Override
    	public void onCreate() {
    		super.onCreate();

    		// This configuration tuning is custom. You can tune every option, you may tune some of them, 
    		// or you can create default configuration by
    		//  ImageLoaderConfiguration.createDefault(this);
    		// method.
    		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
    			.threadPriority(Thread.NORM_PRIORITY - 2)
    			.memoryCacheSize(2 * 1024 * 1024) // 2 Mb
    			.denyCacheImageMultipleSizesInMemory()
    			.discCacheFileNameGenerator(new Md5FileNameGenerator())
    			.imageDownloader(new ExtendedImageDownloader(getApplicationContext()))
    			.tasksProcessingOrder(QueueProcessingType.LIFO)
    			.enableLogging() // Not necessary in common
    			.build();
    		// Initialize ImageLoader with configuration.
    		ImageLoader.getInstance().init(config);
    	}
    }*/
}

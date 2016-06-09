package easyrun.application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

/**
 * A custom application class, which performs setup of the Typefaces used as Icon Sets.
 */
public class Application extends android.app.Application {

    @Override public void onCreate() {
        super.onCreate();
        // setup default typefaces
        TypefaceProvider.registerDefaultIconSets();
    }

}

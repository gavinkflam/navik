package hk.gavin.navik.util;

import com.skobbler.ngx.*;
import com.skobbler.ngx.map.SKMapViewStyle;
import com.skobbler.ngx.navigation.SKAdvisorSettings;
import hk.gavin.navik.application.NKApplication;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import java.io.File;

public class SkobblerUtility {

    public static Promise<Boolean, Void, Void> prepareAndInitializeLibrary() {
        final DeferredObject<Boolean, Void, Void> deferred = new DeferredObject<>();
        final NKApplication application = NKApplication.getInstance();

        if (!new File(application.getMapResourcesPath()).exists()) {
            new SKPrepareMapTextureThread(
                    application, application.getMapResourcesPath(), "SKMaps.zip",
                    new SKPrepareMapTextureListener() {

                        @Override
                        public void onMapTexturesPrepared(boolean prepared) {
                            // Map resources were copied
                            deferred.resolve(initializeLibrary(application));
                        }

                    }
            ).start();
        }
        else {
            deferred.resolve(initializeLibrary(application));
        }

        return deferred;
    }

    public static boolean initializeLibrary(NKApplication application) {
        final String mapResourcesPath = application.getMapResourcesPath();
        SKMapsInitSettings mapsInitSettings = new SKMapsInitSettings();

        mapsInitSettings.setMapResourcesPaths(
                mapResourcesPath, new SKMapViewStyle(mapResourcesPath + "daystyle/", "daystyle.json")
        );

        final SKAdvisorSettings advisorSettings = mapsInitSettings.getAdvisorSettings();
        advisorSettings.setAdvisorConfigPath(mapResourcesPath + "/Advisor");
        advisorSettings.setResourcePath(mapResourcesPath + "/Advisor/Languages");
        advisorSettings.setLanguage(SKAdvisorSettings.SKAdvisorLanguage.LANGUAGE_EN);
        advisorSettings.setAdvisorVoice("en");
        advisorSettings.setAdvisorType(SKAdvisorSettings.SKAdvisorType.TEXT_TO_SPEECH);
        mapsInitSettings.setAdvisorSettings(advisorSettings);
        mapsInitSettings.setPreinstalledMapsPath(mapResourcesPath +"/PreinstalledMaps");

        try {
            SKMaps.getInstance().initializeSKMaps(application, mapsInitSettings);
            return true;
        }
        catch (SKDeveloperKeyException exception) {
            exception.printStackTrace();
            return false;
        }
    }
}

package hk.gavin.navik.util;

import android.widget.Toast;
import com.skobbler.ngx.*;
import com.skobbler.ngx.map.SKMapViewStyle;
import com.skobbler.ngx.navigation.SKAdvisorSettings;
import com.skobbler.ngx.util.SKLogging;
import hk.gavin.navik.application.NKApplication;

import java.io.File;

public class SkobblerUtility {

    public static boolean prepareAndInitializeLibrary() {
        SKLogging.enableLogs(true);
        final NKApplication app = NKApplication.getInstance();

        if (!new File(app.getMapResourcesPath()).exists()) {
            new SKPrepareMapTextureThread(
                    app, app.getMapResourcesPath(), "SKMaps.zip",
                    new SKPrepareMapTextureListener() {

                        @Override
                        public void onMapTexturesPrepared(boolean prepared) {
                            Toast.makeText(app, "Map resources were copied", Toast.LENGTH_SHORT).show();
                            initializeLibrary();
                        }

                    }
            ).start();
        }
        else {
            initializeLibrary();
        }

        return true;
    }

    public static boolean initializeLibrary() {
        final NKApplication app = NKApplication.getInstance();
        final String mapResourcesPath = app.getMapResourcesPath();
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

        try {
            SKMaps.getInstance().initializeSKMaps(app, mapsInitSettings);
            return true;
        }
        catch (SKDeveloperKeyException exception) {
            exception.printStackTrace();
            return false;
        }
    }


}

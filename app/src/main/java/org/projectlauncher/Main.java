package org.projectlauncher;
import org.projectlauncher.gui.LauncherInterfaceMain;
import org.projectlauncher.gui.loader.LoadingFrame;
import org.projectlauncher.instances.Instance;
import org.projectlauncher.launch.LaunchManager;
import org.projectlauncher.setup.FolderSetup;

import java.io.File;
import java.nio.file.Path;
public class Main {
    private static final File BASE_FOLDER =
            new File("launcher-data");
    private static final File CACHE_FOLDER =
            new File(BASE_FOLDER, "cache");

    private static final File DOWNLOADS_FOLDER =
            new File(BASE_FOLDER, "downloads");

    private static final File NATIVES_FOLDER =
            new File(BASE_FOLDER, "natives");

    private static final File ASSETS_FOLDER =
            new File(BASE_FOLDER, "assets");


    public static void main(String[] args) {

        LoadingFrame loader = new LoadingFrame();
        System.out.println(
                "ARCH: " + System.getProperty("os.arch")
        );

        loader.launchLoader();

        FolderSetup.initializeFolders();

        LauncherInterfaceMain ui =
                new LauncherInterfaceMain();

        ui.launchInterface();

        loader.destroy();
    }


    public static void launchInstance(Instance instance) {

        try {

            LaunchManager.launch(
                    instance.version,
                    instance,
                    CACHE_FOLDER.toPath(),
                    DOWNLOADS_FOLDER.toPath(),
                    ASSETS_FOLDER.toPath(),
                    NATIVES_FOLDER.toPath()
            );

        } catch (Exception e) {

            System.err.println("Launch failed:");
            e.printStackTrace();
        }
    }
}
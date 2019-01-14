package runner;

import org.junit.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static step.AppiumStepDefinitions.startServer;
import static util.AppiumUtil.stopAppiumServer;
import static util.EnvironmentUtil.*;
import static util.LoggingUtil.LOGGER;

public class AppiumServerRunner {

    static class open {

        public static void main(String[] args) {

            Map<String, String> dataMap = new HashMap<>();

//            dataMap.put( MobileCapabilityType.NO_RESET,             "false" );
//            dataMap.put( MobileCapabilityType.CLEAR_SYSTEM_FILES,   "true" );
            dataMap.put( "port", APPIUM_PORT );
            dataMap.put( "host", APPIUM_HOST );

            dataMap.put("platformName", "Android");
            dataMap.put( "platformVersion", "9" );
            dataMap.put( "browserName", "Chrome" );
            dataMap.put( "deviceName", "127.0.0.1:5554" );

            startServer( dataMap );

//            AppiumServiceBuilder builder = new AppiumServiceBuilder();
//
//            Map<String, String> dataMap = new HashMap<>();
//
//            desiredCapabilities = new DesiredCapabilities();
//
//            dataMap.put( MobileCapabilityType.NO_RESET,             "false" );
//            dataMap.put( MobileCapabilityType.CLEAR_SYSTEM_FILES,   "true" );
//
//            setDesiredCapabilities( desiredCapabilities, dataMap );
//
//            if( isPortAvailable( Integer.parseInt( APPIUM_PORT ), true ) )
//                startAppiumServer( builder, desiredCapabilities, Integer.parseInt( APPIUM_PORT ) );
//
//            else {
//
//                LOGGER.error( "Appium Server already running on Port - " + APPIUM_PORT );
//                Assert.fail();
//
//            }

        }

    }

    static class close {

        public static void main(String[] args) {

            stopAppiumServer( APPIUM_PORT );

        }

    }

    static class emulatorRunner {

        public static void main(String[] args) {

            String emulatorRoot = ENV_MAP.get( "ANDROID_EMULATOR_ROOT" );

            executeRuntimeCommand( emulatorRoot );

        }

        private static void executeRuntimeCommand(String emulatorRoot) {

            Process process = null;

            try {

                File emulatorFile = new File( emulatorRoot );

                process = Runtime.getRuntime().exec( "./emulator -avd Nexus_5_API_28", null, emulatorFile );

            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail();
            }

            BufferedReader reader = new BufferedReader( new InputStreamReader( process.getInputStream() ) );

            String line = "";

            while ( true ) {

                try {

                    if ( ( line = reader.readLine() ) == null ) break;

                } catch (IOException e) {

                    Assert.fail();

                }

                LOGGER.info( line + "\n" );

            }

            try {

                process.waitFor();

            } catch (InterruptedException e) {

                Assert.fail();

            }

        }

    }

}

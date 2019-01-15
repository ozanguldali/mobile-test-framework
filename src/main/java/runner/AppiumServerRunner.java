package runner;

import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

import static util.EnvironmentUtil.*;

public class AppiumServerRunner {

    private static AppiumDriverLocalService service;

    static class open {

        private static List<String> readerList = new LinkedList<>();

        public static void main(String[] args) {

            AppiumServiceBuilder builder = new AppiumServiceBuilder();

            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

            desiredCapabilities.setCapability( MobileCapabilityType.NO_RESET, "false" );
            desiredCapabilities.setCapability( MobileCapabilityType.CLEAR_SYSTEM_FILES, true );

            if( isPortAvailableScript( Integer.parseInt( APPIUM_PORT ), true ) ) {

                ENV_MAP.put( "PATH", "/usr/local/bin:" + ENV_MAP.get( "PATH" ) );

                builder.withIPAddress( APPIUM_HOST );
                builder.usingPort( Integer.parseInt( APPIUM_PORT ) );
                builder.withCapabilities( desiredCapabilities );
                builder.withArgument( GeneralServerFlag.SESSION_OVERRIDE );
                builder.withArgument( GeneralServerFlag.LOG_LEVEL,"error" );
                builder.withEnvironment( ENV_MAP );

                service = AppiumDriverLocalService.buildService( builder );
                service.start();

            } else {

               System.out.println( "Appium Server already running on Port - " + APPIUM_PORT);
               System.exit( 0 );

            }

        }

        static boolean isPortAvailableServer(int port, boolean kill) {

            ServerSocket serverSocket = null;

            try {

                serverSocket = new ServerSocket();

                if ( OS_VALUE.toLowerCase().contains( "mac" ) )
                    serverSocket.setReuseAddress( false );

                serverSocket.bind( new InetSocketAddress( InetAddress.getByName( "localhost" ), port ), 1 );

                serverSocket.close();

                System.out.println( String.format( "\tThe port is available: [%d]\t\n", port ) );

                return true;

            } catch (Exception e) {

                System.out.println( String.format( "\tThe port is NOT available: [%d]\t\n", port ) );

                if ( kill ) {

                    killPort( port );
                    System.out.println( String.format( "\tKilling the port: [%d]\t\n", port ) );
                    waitForNSeconds( 2 );
//                standPort( port );
//                waitForNSeconds( 2 );

                isPortAvailableServer( port, false );

                }

                return false;

            }

        }

        private static boolean isPortAvailableScript(int port, boolean kill) {

            executeRuntimeCommand( "lsof -t -i:" + port );

            boolean available = true;

            for ( String line : readerList ) {

                if ( line.equals( String.valueOf( port ) ) )
                    available = false;

            }

            if ( available ) {

                System.out.println( String.format( "\tThe port is available: [%d]\t\n", port ) );

                return true;

            } else {

                System.out.println( String.format( "\tThe port is NOT available: [%d]\t\n", port ) );

                if ( kill ) {

                    killPort( port );
                    System.out.println( String.format( "\tKilling the port: [%d]\t\n", port ) );
                    waitForNSeconds( 2 );
    //                standPort( port );
    //                waitForNSeconds( 2 );

                    isPortAvailableScript( port, false );

                }

                return false;
            }

        }

        private static void standPort(int port) {

            String command = "netstat -a -b -l | grep \"" + port + "\"";

            executeRuntimeCommand( command );

        }

        static void killPort(int port) {


            String command = "kill -9 $(lsof -t -i:" + port + ")";

            executeRuntimeCommand( command );

        }

        static void executeRuntimeCommand(String command) {

            Process process = null;

            try {

                process = Runtime.getRuntime().exec( command );

            } catch (Exception e) {
                e.printStackTrace();
                System.exit( 0 );
            }

            printExecutedCommandResult(process);

        }

        private static void printExecutedCommandResult(Process process) {

            BufferedReader reader = new BufferedReader( new InputStreamReader( process.getInputStream() ) );

            String line = "";

            while ( true ) {

                try {

                    if ( ( line = reader.readLine() ) == null ) break;

                    else
                        readerList.add( line );

                } catch (IOException e) {

                    System.exit( 0 );

                }

                System.out.println( line + "\n" );

            }

            try {

                process.waitFor();

            } catch (InterruptedException e) {

                System.exit( 0 );

            }

        }

        private static void waitForNSeconds(long seconds) {

            try {

                Thread.sleep( seconds * 1000L );
                System.out.println( "\tWait for [" + seconds + "] seconds\t\n" );

            } catch (Exception e) {

                System.out.println( "\tError during waiting for [" + seconds + "] seconds.\t\n" );
                e.printStackTrace();

            }

        }

    }

    static class close {

        public static void main(String[] args) {

            try {

                service.stop();

                System.out.println( "\tThe server is closed.\t\n" );

            } catch (Exception e) {

                System.out.println( "\tThe server could NOT been closed.\t\n" );
                e.printStackTrace();
                System.exit( 0 );

            }

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
                System.exit( 0 );
            }

            open.printExecutedCommandResult(process);

        }

    }

}

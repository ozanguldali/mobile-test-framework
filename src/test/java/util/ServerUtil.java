package util;

import org.junit.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import static step.CommonStepDefinitions.waitForNSeconds;
import static util.EnvironmentUtil.OS_VALUE;
import static util.LoggingUtil.LOGGER;

public class ServerUtil {

    public static boolean isPortAvailable(int port, boolean kill) {

        ServerSocket serverSocket = null;

        try {

            serverSocket = new ServerSocket();

            if ( OS_VALUE.toLowerCase().contains( "mac" ) )
                serverSocket.setReuseAddress( false );

            serverSocket.bind( new InetSocketAddress( InetAddress.getByName( "localhost" ), port ), 1 );

            LOGGER.info( String.format( "\tThe port is available: [%d]\t\n", port ) );
            return true;

        } catch (Exception e) {

            LOGGER.info( String.format( "\tThe port is NOT available: [%d]\t\n", port ) );

            if ( kill ) {

                killPort( port );
                LOGGER.info( String.format( "\tKilling the port: [%d]\t\n", port ) );
                waitForNSeconds( 2 );
//                standPort( port );
//                waitForNSeconds( 2 );
//                isPortAvailable( port, false );

            }

            return false;

        }

    }

    private static void standPort(int port) {

        String command = "netstat -a -b -l | grep \"" + port + "\"";

        executeRuntimeCommand( command );

    }

    public static void killPort(int port) {


        String command = "kill -9 $(lsof -t -i:" + port + ")";

        executeRuntimeCommand( command );

    }

    private static void executeRuntimeCommand(String command) {

        Process process = null;

        try {

            process = Runtime.getRuntime().exec( command );

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

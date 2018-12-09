package org.maven.aws.s3;

import org.apache.maven.wagon.AbstractWagon;
import org.apache.maven.wagon.ConnectionException;
import org.apache.maven.wagon.TransferFailedException;
import org.apache.maven.wagon.ResourceDoesNotExistException;
import org.apache.maven.wagon.authentication.AuthenticationException;
import org.apache.maven.wagon.authorization.AuthorizationException;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * S3 Wagon
 */
public class S3Wagon extends AbstractWagon
{

    private static final String PATH = "s3://bhp-maven-repo/release";
    Logger logger = Logger.getLogger( S3Wagon.class.getName() );

    @Override
    protected void openConnectionInternal()
            throws ConnectionException, AuthenticationException
    {
    }

    @Override
    protected void closeConnection()
            throws ConnectionException
    {
    }

    public void get( String resourceName, File destination )
            throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException
    {
        String command = String.format("aws s3 cp %s/%s %s",  PATH, resourceName, destination.getAbsolutePath() );
        logger.info( "Get Command : " + command );
        execute(command);
    }

    public boolean getIfNewer( String resourceName, File destination, long timestamp )
            throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException
    {
        return false;
    }

    public void put( File source, String destination )
            throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException
    {
        String command = String.format("aws s3 cp %s %s/%s", source.getAbsolutePath(), PATH, destination);
        logger.info( "Put Command : " + command );
        execute(command);
    }

    private void execute(String command) {
        try {
            Process process = Runtime.getRuntime().exec( command );
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

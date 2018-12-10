package org.maven.aws.s3;

import org.apache.maven.wagon.AbstractWagon;
import org.apache.maven.wagon.ConnectionException;
import org.apache.maven.wagon.TransferFailedException;
import org.apache.maven.wagon.ResourceDoesNotExistException;
import org.apache.maven.wagon.authentication.AuthenticationException;
import org.apache.maven.wagon.authorization.AuthorizationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * S3 Wagon
 */
public class S3Wagon extends AbstractWagon
{

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
        String command = String.format("aws s3 cp %s/%s %s",
                getRepository().getUrl(), resourceName, destination.getAbsolutePath() );
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
        String command = String.format("aws s3 cp %s %s/%s",
                source.getAbsolutePath(), getRepository().getUrl(), destination);
        logger.info( "Put Command : " + command );
        execute(command);
    }

    private void execute(String command) throws TransferFailedException {
        try {
            Process process = Runtime.getRuntime().exec( command );
            try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            ) {
                String line = null;
                while ( ( line = reader.readLine() ) != null ) {
                    logger.info( line );
                    if ( line.toLowerCase().indexOf("error") >= 0 && !command.contains("maven-metadata")) {
                        throw new TransferFailedException( "Error while transferring files " + line );
                    }
                }
            }
            process.waitFor();
        } catch ( IOException | InterruptedException e ) {
            throw new TransferFailedException( e.getMessage() );
        }
    }
}

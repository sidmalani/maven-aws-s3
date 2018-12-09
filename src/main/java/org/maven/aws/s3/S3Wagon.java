package org.maven.aws.s3;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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

    @Override
    public void get( String resourceName, File destination )
            throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException
    {
        String command = String.format("aws s3 cp %s/%s %s",  PATH, resourceName, destination.getAbsolutePath() );
        logger.info( "Get Command : " + command );
        execute(command);
    }

    @Override
    public boolean getIfNewer( String resourceName, File destination, long timestamp )
            throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException
    {
        return false;
    }

    @Override
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

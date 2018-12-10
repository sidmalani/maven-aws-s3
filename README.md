# maven-aws-s3
Maven extension for S3

Maven extension for S3 is slightly different from other similar extensions such as kuali and aws.maven.
This extension uses the aws cli .aws/credentials for authentication and hence prevents you from having to expose the S3 bucket via HTTP. All you need to do is create a bucket and ensure you have configured your aws cli.

If you have configured the aws cli or you have got the federated login set up correctly then this extension should just work.

Modify your ~/.m2/settings.xml to include S3 server

```xml
  <servers>
    <server>
      <id>reponame</id>
    </server> 
  </servers>
```

Modify your pom.xml - add the following to use the plugin to deploy.

```xml
  <distributionManagement>
    <repository>
      <id>reponame</id>
      <name>My artifactory</name>
      <url>s3://bucketname/release</url>
    </repository>
  </distributionManagement>

  <build>
    <extensions>
      <extension>
        <groupId>io.github.sidmalani</groupId>
        <artifactId>wagon-s3</artifactId>
        <version>0.2</version>
      </extension>
    </extensions>
  </build>
```

To use the S3 artifactory add the following

```xml
  <repositories>
    <repository>
      <id>reponame</id>
      <name>My artifactory</name>
      <url>s3://bucketname/release</url>
    </repository>
  </repositories>
```

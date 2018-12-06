//package com.scalefocus.shop.camel;
//
//import org.apache.camel.CamelContext;
//import org.apache.camel.Endpoint;
//import org.apache.camel.LoggingLevel;
//import org.apache.camel.builder.RouteBuilder;
//import org.apache.camel.util.FileUtil;
//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPClientConfig;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CamelFTPRoute  extends RouteBuilder {
//
//    private static final Logger logger = LoggerFactory.getLogger(CamelFTPRoute.class);
//
//    @Autowired
//    private Environment env;
//
//    private static final String SFTP_HOST = "sftp.host";
//    private static final String SFTP_PORT = "sftp.port";
//    private static final String SFTP_USERNAME = "sftp.username";
//    private static final String SFTP_PASSWORD = "sftp.password";
//
//    private Endpoint sftpEndpoint;
//    private Endpoint fileEndpoint;
//    private Endpoint streamEndpoint;
//
//    public void uploadFile(String fileName) {
//        configureFileEndpoint(fileName);
//        configureSFTPEndpoint();
//        try {
//            configure();
//        } catch (Exception e) {
//            logger.error(e.getMessage());   // TODO: Appropriate exception handling...
//        }
//    }
//
//    @Override
//    public void configure() throws Exception {
//        onCompletion().log("File uploaded successfully!");
//
//        // TODO: Update the actual camel route endpoints later.
//        // TODO: Externalize some configuration properties (e.g. username, password, etc...).
//
////        from("file://src/main/resources/images?noop=true")
////                .to("stream:file?fileName=TEMP")
////                .log("fuckxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
////                .end();
////
////        from("stream:file//:src/main/resources/images")
////                .log(LoggingLevel.INFO, logger, "FTP processing has started...")
////                .to(sftpEndpoint)
////                .end();
//////
//        from(fileEndpoint)
//                .log(LoggingLevel.INFO, logger, "FTP processing has started...")
//                .to(sftpEndpoint)
//                .end();
//    }
//
//    public void configureSFTPEndpoint() {
//        String host = env.getProperty(SFTP_HOST);
//        String port = env.getProperty(SFTP_PORT);
//        String username = env.getProperty(SFTP_USERNAME);
//        String password = env.getProperty(SFTP_PASSWORD);
//
//        sftpEndpoint = endpoint(String.format("sftp://%s:%s@%s:%s/../../uploads", username, password, host, port));
//    }
//
//    public void configureFileEndpoint(String fileName) {
//        fileEndpoint = endpoint("file://src/main/resources/images/fileName=" + fileName + "?noop=true");
//    }
//
//    public void configureStreamEndpoint() {
////        streamEndpoint = endpoint("stream:in");
//    }
//}

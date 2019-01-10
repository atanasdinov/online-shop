package com.scalefocus.shop.service.sftp;

import com.jcraft.jsch.*;
import com.scalefocus.shop.service.ProductService;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

/**
 * SftpClient component for uploading, downloading and deleting files from remote sftp server.
 */
@Component
public class SftpClient {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private static final String SFTP_HOST = "sftp.host";
    private static final String SFTP_PORT = "sftp.port";
    private static final String SFTP_USERNAME = "sftp.username";
    private static final String SFTP_PASSWORD = "sftp.password";
    private static final String SFTP_DESTINATION_PATH = "sftp.destination.path";

    private String serverAddress;
    private Integer serverPort;
    private String username;
    private String password;
    private String destinationPath;

    @Autowired
    private Environment env;

    private void configureSftp() {
        serverAddress = env.getProperty(SFTP_HOST);
        serverPort = NumberUtils.toInt(env.getProperty(SFTP_PORT));
        username = env.getProperty(SFTP_USERNAME);
        password = env.getProperty(SFTP_PASSWORD);
        destinationPath = env.getProperty(SFTP_DESTINATION_PATH);
    }

    public void uploadFile(MultipartFile file) {
        Session session = null;
        ChannelSftp channelSftp = null;
        configureSftp();

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, serverAddress, serverPort);
            session.setPassword(password);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            channelSftp.cd(destinationPath);
            channelSftp.put(file.getInputStream(), file.getOriginalFilename(), ChannelSftp.OVERWRITE);

            channelSftp.disconnect();
            session.disconnect();
        } catch (IOException | JSchException | SftpException e) {
            logger.error(e.getMessage());
        } finally {
            if (channelSftp != null && channelSftp.isConnected())
                channelSftp.disconnect();

            if (session != null && session.isConnected())
                session.disconnect();
        }
    }

    public String downloadFile(String fileName) {
        Session session = null;
        ChannelSftp channelSftp = null;
        String encodedImage = null;
        configureSftp();

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, serverAddress, serverPort);
            session.setPassword(password);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            channelSftp.cd(destinationPath);
            InputStream inputStream = channelSftp.get(fileName);

            BufferedImage bImage = ImageIO.read(inputStream);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", bos);
            byte[] image = bos.toByteArray();

            Base64.Encoder encoder = Base64.getEncoder();

            encodedImage = "data:image/png;base64," + encoder.encodeToString(image);

            channelSftp.disconnect();
            session.disconnect();
        } catch (IOException | JSchException | SftpException e) {
            logger.error(e.getMessage());
        } finally {
            if (channelSftp != null && channelSftp.isConnected())
                channelSftp.disconnect();

            if (session != null && session.isConnected())
                session.disconnect();
        }
        return encodedImage;
    }

    public void deleteFile(String fileName) {
        Session session = null;
        ChannelSftp channelSftp = null;
        configureSftp();

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, serverAddress, serverPort);
            session.setPassword(password);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            channelSftp.cd(destinationPath);

            channelSftp.rm(fileName);

            channelSftp.disconnect();
            session.disconnect();

        } catch (JSchException | SftpException e) {
            logger.error(e.getMessage());
        } finally {
            if (channelSftp != null && channelSftp.isConnected())
                channelSftp.disconnect();

            if (session != null && session.isConnected())
                session.disconnect();
        }
    }
}
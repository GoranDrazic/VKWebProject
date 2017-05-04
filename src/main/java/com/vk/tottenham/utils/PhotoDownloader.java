package com.vk.tottenham.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.vk.tottenham.core.model.VKContext;
import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.vk.VkGateway;
import com.vk.tottenham.vk.model.PhotoDescription;
import com.vk.tottenham.vk.model.UploadResult;

@Component("photoDownloader")
public class PhotoDownloader {
    private static final Logger LOGGER = Logger.getLogger(PhotoDownloader.class);

    @Autowired
    private VKContext vkContext;
    @Autowired
    private VkGateway vkGateway;
    
    private static HttpClient client = HttpClientBuilder.create().build();

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    public PhotoDescription downloadPhoto(String href, boolean isTestMode) {
        return downloadPhoto(href, vkContext.getAlbumId(isTestMode), isTestMode);
    }

    public PhotoDescription downloadPhoto(String href, String albumId, boolean isTestMode) {
        return downloadPhoto(href, albumId, isTestMode, null);
    }

    public PhotoDescription downloadPhoto(String href, String albumId, boolean isTestMode, String description) {
        String uploadUrl = vkGateway.getUploadServer(vkContext.getGroupId(isTestMode),
                albumId);

        String imageId = getImageId(href);

        return uploadPhoto(href, imageId, uploadUrl, albumId, isTestMode, description);
    }

    private String getImageId(String href) {
        String imageId = href;
        if (imageId.contains("?")) {
            imageId = imageId.substring(0, imageId.indexOf("?"));
        }
        return imageId.substring(imageId.lastIndexOf("/") + 1);
    }

    private PhotoDescription uploadPhoto(String url, String localFileName, String uploadUrl, String albumId, boolean isTestMode, String description) {
        PhotoDescription photo = new PhotoDescription();

        String localPhoto = "/Users/alexandrfeskoff/Downloads/" + localFileName;
        LOGGER.info("Loading photo: " + localPhoto);
        saveImage(url, localPhoto);
        
        BufferedImage bimg;
        try {
            bimg = ImageIO.read(new File(localPhoto));
            photo.setWidth(bimg.getWidth());
            photo.setWidth(bimg.getHeight());
        } catch (IOException e) {}

        UploadResult uploadResult = uploadPhotos(uploadUrl, localPhoto);

        deleteLocalPhoto(localPhoto);

        String photoId = vkGateway.savePhoto(uploadResult.getServer(),
                uploadResult.getPhotosList(), uploadResult.getAid(),
                uploadResult.getHash(), albumId, vkContext.getGroupId(isTestMode), description);
        photo.setPhotoId(photoId);

        return photo;
    }

    private void deleteLocalPhoto(String localPhoto) {
        File local = new File(localPhoto);
        local.delete();
    }

    private void saveImage(String imageUrl, String destinationFile) {
        try (InputStream inputStream = new URL(
                imageUrl.trim().replace(" ", "%20").replaceAll("\\n", ""))
                        .openStream();
                ReadableByteChannel readableByteChannel = Channels
                        .newChannel(inputStream);
                FileOutputStream fileOutputStream = new FileOutputStream(
                        destinationFile)) {
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0,
                    1 << 24);
        } catch (Exception e) {
            throw new VkException("Error downloading an image", e);
        }
    }

    private static UploadResult uploadPhotos(String uploadUrl, String localPhoto) {
        try {
            HttpPost uploadFile = new HttpPost(uploadUrl);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            File f = new File(localPhoto);
            builder.addBinaryBody("file1", new FileInputStream(f),
                    ContentType.APPLICATION_OCTET_STREAM, f.getName());
            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);
            HttpResponse response = client.execute(uploadFile);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            response.getEntity().getContent().close();
            return mapper.readValue(result.toString(),
                    new TypeReference<UploadResult>() {
                    });
        } catch (Exception e) {
            throw new VkException("Exception updaloding a photo", e);
        }
    }
}

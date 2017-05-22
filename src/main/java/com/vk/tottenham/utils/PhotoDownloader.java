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
import com.vk.tottenham.vk.model.UploadCoverResult;
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
        return downloadPhoto(href, vkContext.getAlbumId(isTestMode), isTestMode, false);
    }

    public PhotoDescription downloadPhoto(String href, String albumId, boolean isTestMode, boolean useMainGroup) {
        return downloadPhoto(href, albumId, isTestMode, null, useMainGroup);
    }

    public PhotoDescription downloadPhoto(String href, String albumId, boolean isTestMode, String description, boolean useMainGroup) {
        String groupId = useMainGroup ? vkContext.getGroupId(isTestMode) : vkContext.getMediaGroupId(isTestMode);
        String uploadUrl = vkGateway.getUploadServer(groupId, albumId);

        String imageId = getImageId(href);

        return uploadPhoto(href, imageId, uploadUrl, albumId, groupId, description);
    }

    public void uploadCoverPhoto(String groupId, String localCoverPhoto) {
        String uploadUrl = vkGateway.getOwnerCoverPhotoUploadServer(groupId, 0, 0, 1590, 400);

        UploadCoverResult uploadResult = uploadPhotos(uploadUrl, localCoverPhoto, UploadCoverResult.class);
        
        vkGateway.saveOwnerCoverPhoto(uploadResult.getPhoto(), uploadResult.getHash());
    }

    private String getImageId(String href) {
        String imageId = href;
        if (imageId.contains("?")) {
            imageId = imageId.substring(0, imageId.indexOf("?"));
        }
        return imageId.substring(imageId.lastIndexOf("/") + 1);
    }

    private PhotoDescription uploadPhoto(String url, String localFileName, String uploadUrl, String albumId, String groupId, String description) {
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

        UploadResult uploadResult = uploadPhotos(uploadUrl, localPhoto, UploadResult.class);

        deleteLocalPhoto(localPhoto);

        String photoId = vkGateway.savePhoto(uploadResult.getServer(),
                uploadResult.getPhotosList(), uploadResult.getAid(),
                uploadResult.getHash(), albumId, groupId, description);
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

    private static <T> T uploadPhotos(String uploadUrl, String localPhoto, Class<T> responseClass) {
        try {
            HttpPost uploadFile = new HttpPost(uploadUrl);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            File f = new File(localPhoto);
            String param = responseClass.equals(UploadResult.class) ? "file1" : "photo";
            builder.addBinaryBody(param, new FileInputStream(f),
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
            return mapper.readValue(result.toString(), responseClass);
        } catch (Exception e) {
            throw new VkException("Exception updaloding a photo", e);
        }
    }
}

package com.vk.tottenham.vk;

import java.awt.Desktop;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.HttpResponse;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import com.vk.tottenham.exception.VkException;
import com.vk.tottenham.vk.model.CreateAlbumResponseWrapper;
import com.vk.tottenham.vk.model.GetAlbumsResponseWrapper;
import com.vk.tottenham.vk.model.GetHistoryResponseWrapper;
import com.vk.tottenham.vk.model.GetPageResponse;
import com.vk.tottenham.vk.model.GetPageResponseWrapper;
import com.vk.tottenham.vk.model.GetPhotosResponseWrapper;
import com.vk.tottenham.vk.model.GetPostsResponseWrapper;
import com.vk.tottenham.vk.model.GetUploaderServerResponseWrapper;
import com.vk.tottenham.vk.model.GetUsersResponseWrapper;
import com.vk.tottenham.vk.model.Message;
import com.vk.tottenham.vk.model.Photo;
import com.vk.tottenham.vk.model.Post;
import com.vk.tottenham.vk.model.SavePageResponseWrapper;
import com.vk.tottenham.vk.model.SavePhotosResponse;
import com.vk.tottenham.vk.model.SendMessageResponseWrapper;
import com.vk.tottenham.vk.model.User;
import com.vk.tottenham.vk.model.WallPostResponseWrapper;

@Component("vkGateway")
public class VkGateway {
    @Value("${com.vk.tottenham.vk.accessToken}")
    private String accessToken;

    @Value("${com.vk.tottenham.vk.apiId}")
    private String apiId;

    @Value("${com.vk.tottenham.vk.url}")
    private String vkApiUrl;

    @Autowired
    @Qualifier("vkRestTemplate")
    private RestOperations vkRestTemplate;
    
    @Autowired
    @Qualifier("vkHttpClient")
    private HttpClient vkHttpClient;

    private static final String API_VERSION = "5.62";
    private static final String AUTH_URL = "https://oauth.vk.com/authorize"
            + "?client_id={APP_ID}" + "&scope={PERMISSIONS}"
            // + "&group_ids={GROUP_IDS}"
            + "&redirect_uri={REDIRECT_URI}" + "&display={DISPLAY}"
            + "&v={API_VERSION}" + "&response_type=code&state=Idoesntfuckingwork";

    private VkObjectMapper vkObjectMapper = new VkObjectMapper();

    public VkGateway() {}

    public String createAlbum(String groupId, String title, String description) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("title", title));
        params.add(new BasicNameValuePair("group_id", groupId));
        params.add(new BasicNameValuePair("description", description));
        params.add(new BasicNameValuePair("upload_by_admins_only", "1"));
        CreateAlbumResponseWrapper response = invokeOldApi("photos.createAlbum", params, CreateAlbumResponseWrapper.class);
        return response.getResponse().getId();

    }
    
    public String getUploadServer(String groupId, String albumId) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("album_id", albumId));
        params.add(new BasicNameValuePair("group_id", groupId));
        GetUploaderServerResponseWrapper response = invokeOldApi("photos.getUploadServer", params, GetUploaderServerResponseWrapper.class);
        return response.getResponse().getUploadUrl();
    }

    public void postOnWall(String groupId, String message, String photoId, long lastPostDate) {
        List<String> photoIds = new LinkedList<>();
        photoIds.add(photoId);
        postOnWall(groupId, message, photoIds, lastPostDate);
    }

    public void postOnWall(String groupId, String message, List<String> photoIds, long lastPostDate) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("owner_id", "-" + groupId));
        params.add(new BasicNameValuePair("from_group", "1"));
        params.add(new BasicNameValuePair("message", message));
        if (lastPostDate != -1) {
            params.add(new BasicNameValuePair("publish_date", "" + lastPostDate / 1000));
        }
        List<String> attachments = new LinkedList<>();
        for (String photoId : photoIds) {
            attachments.add("photo-" + groupId + "_" + photoId);
        }
        params.add(new BasicNameValuePair("attachments", StringUtil.join(attachments, ",")));
        invokeOldApi("wall.post", params, WallPostResponseWrapper.class);
    }
    
    public void sendChatMessage(String message, String chatId) {
        sendChatMessage(message, chatId, null, null);
    }
    
    public void sendChatMessage(String message, String chatId, String photoId, String groupId) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("peer_id", chatId));
        params.add(new BasicNameValuePair("message", message));
        if (photoId != null) {
            params.add(new BasicNameValuePair("attachment", "photo-" + groupId + "_" + photoId));
        }
        invokeOldApi("messages.send", params, SendMessageResponseWrapper.class);
    }

    public List<Message> getChatMessages(String chatId) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("peer_id", chatId));
        params.add(new BasicNameValuePair("count", "10"));
        //params.add(new BasicNameValuePair("start_message_id", start_message_id));
        GetHistoryResponseWrapper response = invokeOldApi("messages.getHistory", params, GetHistoryResponseWrapper.class);
        return response.getResponse().getItems();
    }

    public User getUser(long userId) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("user_ids", userId + ""));
        GetUsersResponseWrapper response = invokeOldApi("users.get", params, GetUsersResponseWrapper.class);
        List<User> users = response.getResponse();
        return users.size() > 0 ? users.get(0) : null;
    }
    
    public String savePhoto(String server, String photosList, String aid,
            String hash, String albumId, String groupId) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("server", server));
        params.add(new BasicNameValuePair("photos_list", photosList));
        params.add(new BasicNameValuePair("aid", aid));
        params.add(new BasicNameValuePair("hash", hash));
        params.add(new BasicNameValuePair("album_id", albumId));
        params.add(new BasicNameValuePair("group_id", groupId));
        SavePhotosResponse response = invokeOldApi("photos.save", params, SavePhotosResponse.class);
        return response.getResponse().get(0).getId();
        
    }
    
    public List<Photo> getPhotos(String groupId, String albumId) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("owner_id", "-" + groupId));
        params.add(new BasicNameValuePair("album_id", albumId));
        params.add(new BasicNameValuePair("count", "100"));
        GetPhotosResponseWrapper response = invokeOldApi("photos.get", params, GetPhotosResponseWrapper.class);
        return response.getResponse().getItems();
    }

    public int getAlbumSize(String groupId, String albumId) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("owner_id", "-" + groupId));
        params.add(new BasicNameValuePair("album_id", albumId));
        params.add(new BasicNameValuePair("count", "500"));
        GetAlbumsResponseWrapper response = invokeOldApi("photos.get", params, GetAlbumsResponseWrapper.class);
        return response.getResponse().getItems().size();
    }

    public List<Post> getScheduledPosts(String groupId) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("owner_id", "-" + groupId));
        params.add(new BasicNameValuePair("filter", "postponed"));
        params.add(new BasicNameValuePair("count", "100"));
        params.add(new BasicNameValuePair("extended", "0"));
        GetPostsResponseWrapper response = invokeOldApi("wall.get", params, GetPostsResponseWrapper.class);
        return response.getResponse().getItems();
    }

    public int savePage(String groupId, String pageId, String title, String text) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("group_id", groupId));
        if (pageId != null) {
            params.add(new BasicNameValuePair("page_id", pageId));
        }
        if (title != null) {
            params.add(new BasicNameValuePair("title", title));
        }
        params.add(new BasicNameValuePair("text", text));
        SavePageResponseWrapper response = invokeOldApi("pages.save", params, SavePageResponseWrapper.class);
        return response.getResponse();
    }

    public GetPageResponse getPage(String groupId, String pageId) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("owner_id", "-" + groupId));
        params.add(new BasicNameValuePair("page_id", pageId));
        params.add(new BasicNameValuePair("need_source", "1"));
        params.add(new BasicNameValuePair("need_html", "0"));
        GetPageResponseWrapper response = invokeOldApi("pages.get", params, GetPageResponseWrapper.class);
        return response.getResponse();
    }

    private void auth(String appId) {
        String reqUrl = AUTH_URL.replace("{APP_ID}", appId)
                .replace("{PERMISSIONS}", "photos,messages,manage")
                .replace("{REDIRECT_URI}", "https://oauth.vk.com/blank.html")
                .replace("{DISPLAY}", "page").replace("{API_VERSION}", API_VERSION);
        try {
            Desktop.getDesktop().browse(new URL(reqUrl).toURI());
        } catch (Exception e) {
            throw new VkException("Exception communication with vk", e);
        }
    }

    private <T> T invokeApi(String method, MultiValueMap<String, String> params, Class<T> responseType) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(vkApiUrl + method);
        builder.queryParam("access_token", accessToken);
        params.add("v", API_VERSION);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        ResponseEntity<T> response = vkRestTemplate.postForEntity(builder.build().encode().toUri(), request, responseType);
        return response.getBody();
    }

    private <T> T invokeOldApi(String method, List<NameValuePair> params, Class<T> responseType) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(vkApiUrl + method);
            builder.queryParam("access_token", accessToken);
            HttpPost post = new HttpPost(builder.build().encode().toUri());
            
            params.add(new BasicNameValuePair("v", API_VERSION));
            
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            
            HttpResponse  httpResponse = vkHttpClient.execute(post);
            
            T response = vkObjectMapper.readValue(httpResponse.getEntity().getContent(), responseType);
            
            return response;
        } catch (Exception e) {
            throw new VkException("Error communication with vk.", e);
        }
    }

    public static void main(String args[]) {//5566318
            String reqUrl = AUTH_URL.replace("{APP_ID}", "5856787")
                    .replace("{PERMISSIONS}", "offline,photos,messages,manage,pages,wall")
                    .replace("{REDIRECT_URI}", "https://oauth.vk.com/blank.html")
                    //.replace("{REDIRECT_URI}", "https://www.tut.by/")
                    .replace("{DISPLAY}", "page").replace("{API_VERSION}", API_VERSION);
            try {
                Desktop.getDesktop().browse(new URL(reqUrl).toURI());
            } catch (Exception e) {
                throw new VkException("Exception communication with vk", e);
            }

        //
    }
}

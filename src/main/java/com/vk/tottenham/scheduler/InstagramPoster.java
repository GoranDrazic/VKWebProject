package com.vk.tottenham.scheduler;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.vk.tottenham.core.model.Player;
import com.vk.tottenham.core.model.ResourceType;
import com.vk.tottenham.mybatis.service.PlayerService;
import com.vk.tottenham.mybatis.service.ResourceService;
import com.vk.tottenham.utils.Icon;
import com.vk.tottenham.utils.PhotoDownloader;

public class InstagramPoster extends SchedulerBase {
    @Autowired
    @Qualifier("resourceService")
    private ResourceService instagramService;
    @Autowired
    @Qualifier("playerService")
    private PlayerService playerService;
    @Autowired
    private PhotoDownloader photoDownloader;

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    @Override
    public void execute() {
        try {
            Map<String, String> photoIds = new HashMap<>();
            List<Player> players = playerService.findAll();
            for (Player player : players) {
                if (player.getInstagram() != null) {
                    Document document = Jsoup.parse(
                            new URL("https://www.instagram.com/" + player.getInstagram() + "/"), 10000);
                    Elements scripts = document.getElementsByTag("body").first()
                            .getElementsByTag("script");
                    String jsonString = "{}";

                    for (Element script : scripts) {
                        if (script.data().contains("window._sharedData")) {
                            jsonString = script.data().replace("window._sharedData = ", "");
                            Map<String, Object> imgJson = mapper.readValue(jsonString,
                                    new TypeReference<HashMap<String, Object>>() {
                                    });
                            Map<String, Object> o = ((Map<String, Object>) imgJson
                                    .get("entry_data"));
                            o = (Map<String, Object>) ((List<Object>) o.get("ProfilePage"))
                                    .get(0);
                            o = ((Map<String, Object>) o.get("user"));
                            o = ((Map<String, Object>) o.get("media"));
                            for (Map<String, Object> mediaObj : ((List<Map<String, Object>>) o
                                    .get("nodes"))) {
                                String href = mediaObj.get("display_src").toString();
                                Object captionEl = mediaObj.get("caption");
                                String caption = captionEl != null
                                        ? player.getInstagram() + ": "
                                                + captionEl.toString()
                                        : player.getInstagram();
                                int lastSlashIndex = href.lastIndexOf("/");
                                int questionIndex = href.lastIndexOf("?");
                                String photoId = questionIndex >= 0 ? href.substring(lastSlashIndex + 1, questionIndex)
                                        : href.substring(lastSlashIndex + 1);
                                boolean isVideo = Boolean
                                        .valueOf(mediaObj.get("is_video").toString());
                                if (!isVideo && !instagramService.exists(ResourceType.INSTAGRAM.value(), player.getInstagram(), photoId)) {
                                    photoIds.put(photoDownloader.downloadPhoto(href, isTestMode).getPhotoId(), caption);
                                    instagramService.save(ResourceType.INSTAGRAM.value(), player.getInstagram(), photoId);
                                }
                            }
                        }
                    }
                }
            }
            List<String> batch = new LinkedList<>();
            StringBuilder captions = new StringBuilder(Icon.PHOTO.getCode() + " Из социальных сетей.\n");
            for (Entry<String, String> photo : photoIds.entrySet()) {
                batch.add(photo.getKey());
                captions.append("\n").append(photo.getValue());
                if (batch.size() == 10) {
                    captions.append("\n\n#social@tottenham_hotspur");
                    vkGateway.postOnWall(getGroupId(), getMediaGroupId(),
                            captions.toString(),
                            batch, getClosestAvailableDate());
                    vkGateway.sendChatMessage(
                            "Внезапно в отложку добавлены фоточки из соц сетей.",
                            getChatId());
                    batch.clear();
                    captions = new StringBuilder(Icon.PHOTO.getCode() + " Из социальных сетей.\n");
                    Thread.currentThread().sleep(5 * 1000);
                }
            }
            if (batch.size() > 0) {
                captions.append("\n\n#social@tottenham_hotspur");
                vkGateway.postOnWall(getGroupId(), getMediaGroupId(), 
                        captions.toString(),
                        batch, getClosestAvailableDate());
                vkGateway.sendChatMessage(
                        "В отложку добавлены фото из соц сетей. Пожалуйста, проверьте и запостите их.",
                        getChatId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

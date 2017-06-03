package com.vk.tottenham.scheduler;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vk.tottenham.core.model.Player;
import com.vk.tottenham.core.model.ResourceType;
import com.vk.tottenham.instagram.InstagramObjectMapper;
import com.vk.tottenham.instagram.model.InstagramScript;
import com.vk.tottenham.instagram.model.Node;
import com.vk.tottenham.mybatis.service.PlayerService;
import com.vk.tottenham.mybatis.service.ResourceService;
import com.vk.tottenham.utils.Icon;
import com.vk.tottenham.utils.JsoupWrapper;
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
    @Autowired
    @Qualifier("instagramObjectMapper")
    private InstagramObjectMapper mapper;
    @Autowired
    private JsoupWrapper jsoupWrapper;

    @Override
    public void execute() {
        try {
            Map<String, String> photoIds = new LinkedHashMap<>();
            List<Player> players = playerService.findAll();
            for (Player player : players) {
                addPlayerPhotos(player, photoIds);
            }
            List<String> batch = new LinkedList<>();
            StringBuilder captions = new StringBuilder(Icon.PHOTO.getCode() + " Из социальных сетей.\n");
            Iterator<Entry<String, String>> iterator = photoIds.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> photo = iterator.next();
                batch.add(photo.getKey());
                captions.append("\n").append(photo.getValue());
                if (batch.size() == 10 || !iterator.hasNext()) {
                    captions.append("\n\n#social@tottenham_hotspur");
                    vkGateway.postOnWall(getGroupId(), getMediaGroupId(),
                            captions.toString(),
                            batch, getClosestAvailableDate());
                    vkGateway.sendChatMessage(
                            "В отложку добавлены фото из соц сетей. Пожалуйста, проверьте и запостите их.",
                            getChatId());
                    batch.clear();
                    captions = new StringBuilder(Icon.PHOTO.getCode() + " Из социальных сетей.\n");
                    Thread.currentThread().sleep(5 * 1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addPlayerPhotos(Player player, Map<String, String> photoIds) throws IOException {
        if (player.getInstagram() != null) {
            Document document = jsoupWrapper.getDocument("https://www.instagram.com/" + player.getInstagram() + "/");
            Elements scripts = document.getElementsByTag("body").first()
                    .getElementsByTag("script");
            String jsonString = "{}";

            for (Element script : scripts) {
                if (script.data().contains("window._sharedData")) {
                    jsonString = script.data().replace("window._sharedData = ", "");
                    InstagramScript instagramScript = mapper.readValue(jsonString, InstagramScript.class);
                    for (Node node : instagramScript.getEntryData().getProfilePage().get(0).getUser().getMedia().getNodes()) {
                        String photoId = getPhotoId(node.getDisplaySrc());

                        if (!node.isVideo() && !instagramService.exists(ResourceType.INSTAGRAM.value(), player.getInstagram(), photoId)) {
                            photoIds.put(photoDownloader.downloadPhoto(node.getDisplaySrc(), isTestMode).getPhotoId(), getCaption(node, player));
                            instagramService.save(ResourceType.INSTAGRAM.value(), player.getInstagram(), photoId);
                        }
                    }
                }
            }
        }
    }

    private String getCaption(Node node, Player player) {
        Object captionEl = node.getCaption();
        return captionEl != null
                ? player.getInstagram() + ": " + captionEl.toString()
                : player.getInstagram();
    }

    private String getPhotoId(String href) {
        int lastSlashIndex = href.lastIndexOf("/");
        int questionIndex = href.lastIndexOf("?");
        return questionIndex >= 0 ? href.substring(lastSlashIndex + 1, questionIndex)
                : href.substring(lastSlashIndex + 1);
    }
}

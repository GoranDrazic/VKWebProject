package com.vk.tottenham.scheduler;

import static com.vk.tottenham.core.model.Constants.*;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vk.tottenham.core.model.VKContext;
import com.vk.tottenham.vk.VkGateway;
import com.vk.tottenham.vk.model.Post;

public abstract class SchedulerBase {
    @Autowired
    protected VKContext vkContext;
    @Autowired
    protected VkGateway vkGateway;
    protected boolean isTestMode = false;
    
    public abstract void execute();

    public void setTestMode(boolean isTestMode) {
        this.isTestMode = isTestMode;
    }

    public String getGroupId() {
        return vkContext.getGroupId(isTestMode);
    }

    public String getAlbumId() {
        return vkContext.getAlbumId(isTestMode);
    }

    public String getChatId() {
        return vkContext.getChatId(isTestMode);
    }

    protected long getClosestAvailableDate() {
        Date date = new Date(new Date().getTime() + POST_DELAY_IN_MILLIS);
        List<Post> posts = vkGateway.getScheduledPosts(getGroupId());
        if (posts.isEmpty()) {
            return date.getTime();
        }
        int index = 0;
        while(true) {
            if (index == 0) {
                //before first
                if (date.before(new Date(posts.get(0).getDate() * 1000 - TIME_BETWEEN_POST_IN_MILLIS))) {
                    return date.getTime();
                } else {
                    index++;
                }
            } else if (index == posts.size()) {
                //after last
                return new Date(
                        Math.max(
                                posts.get(index - 1).getDate() * 1000
                                        + TIME_BETWEEN_POST_IN_MILLIS,
                                date.getTime())).getTime();
            } else {
                //in the middle
                if (posts.get(index).getDate() - posts.get(index - 1).getDate() < 2 * TIME_BETWEEN_POST_IN_MILLIS / 1000
                        || date.after(new Date(posts.get(index).getDate() * 1000 - TIME_BETWEEN_POST_IN_MILLIS))) {
                    index++;
                } else {
                    return new Date(
                            Math.max(
                                    posts.get(index - 1).getDate() * 1000
                                            + TIME_BETWEEN_POST_IN_MILLIS,
                                    date.getTime())).getTime();
                }
            }
        }
    }

    /*public void setVkContext(VKContext vkContext) {
        this.vkContext = vkContext;
    }*/
}

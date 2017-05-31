package com.vk.tottenham.scheduler;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import org.mockito.Mock;

import com.vk.tottenham.core.model.VKContext;
import com.vk.tottenham.vk.VkGateway;

public class SchedulerBaseTest {

    public static final String GROUP_ID = "45645234625";

    public static final String MEDIA_GROUP_ID = "72345234542";

    public static final String CHAT_ID = "823452345234";

    @Mock
    protected VKContext vkContext;
    @Mock
    protected VkGateway vkGateway;

    public void init()
    {
        when(vkContext.getGroupId(eq(false))).thenReturn(GROUP_ID);
        when(vkContext.getMediaGroupId(eq(false))).thenReturn(MEDIA_GROUP_ID);
        when(vkContext.getChatId(eq(false))).thenReturn(CHAT_ID);
    }
}

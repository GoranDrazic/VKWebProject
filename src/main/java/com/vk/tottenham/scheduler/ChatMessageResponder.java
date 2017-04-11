package com.vk.tottenham.scheduler;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vk.tottenham.vk.model.Message;
import com.vk.tottenham.vk.model.User;

public class ChatMessageResponder extends SchedulerBase {

    private static long lastDate = new Date().getTime() / 1000;
    private static Map<Long, User> userMap = new HashMap<>();

    private static final String FUCK_YOU_MESSAGE = "[id377937124|Бот], иди нахуй!";

    private static final String FUCK_YOU_RESPONSE = "Сам иди нахуй!";
    private static final String EAT_CAKE_RESPONSE = "Сам иди! Пирожки закончились?";
    private static final String DRINK_LARD_RESPONSE = "Выпей сала!";
    private static final String GO_TO_BURYATIA_RESPONSE = "Еби нахуй в Бурятию!";
    private static final String FUCK_YOU_CLONE_RESPONSE = "Иди нахуй, клон!";
    private static final String ALREADY_THERE_RESPONSE = "Уже там!";
    private static final String BLOW_PUTIN_RESPONSE = "Отсоси у Путина!";
    private static final String BLOW_NAZARBAEV_RESPONSE = "Отсоси у Назарбаева!";
    private static final String BLOW_POKEMON_RESPONSE = "Отсоси у Покемонов!";
    private static final String BLOW_TIMOSHENKO_RESPONSE = "Отсоси у Тимошенко!";
    private static final String TIMOSHENKO_WIG_RESPONSE = "Надень парик Тимошенко!";

    private static final String FUCK_SOMEONE_ELSE_MESSAGE = ", иди нахуй!";
    private static final String LIKEWISE_RESPONSE = "Присоединяюсь!";

    private Map<Long, List<String>> messageResponses = new HashMap<>(); 

    public ChatMessageResponder() {
        super();
      //"first_name": "Tottenham",
        messageResponses.put(318538841l, Arrays.asList(FUCK_YOU_RESPONSE, EAT_CAKE_RESPONSE, BLOW_PUTIN_RESPONSE));
      //"first_name": "Леонид",
        messageResponses.put(1719068l, Arrays.asList(FUCK_YOU_RESPONSE, BLOW_PUTIN_RESPONSE, BLOW_POKEMON_RESPONSE));
      //"first_name": "Кирилл",
        messageResponses.put(27725490l, Arrays.asList(FUCK_YOU_RESPONSE, DRINK_LARD_RESPONSE, BLOW_TIMOSHENKO_RESPONSE));
      //"first_name": "Дорджи",
        messageResponses.put(525770l, Arrays.asList(FUCK_YOU_RESPONSE, GO_TO_BURYATIA_RESPONSE, BLOW_NAZARBAEV_RESPONSE));
      //"first_name": "Артём",
        messageResponses.put(26887038l, Arrays.asList(FUCK_YOU_RESPONSE, BLOW_PUTIN_RESPONSE));
      //"first_name": "Teo",
        messageResponses.put(222604413l, Arrays.asList(FUCK_YOU_RESPONSE));
      //"first_name": "Паша",
        messageResponses.put(252361245l, Arrays.asList(FUCK_YOU_RESPONSE, BLOW_PUTIN_RESPONSE));
      //"first_name": "Олександр",
        messageResponses.put(36341809l, Arrays.asList(FUCK_YOU_RESPONSE));
      //"first_name": "Тоха",
        messageResponses.put(6469480l, Arrays.asList(FUCK_YOU_RESPONSE, DRINK_LARD_RESPONSE, BLOW_TIMOSHENKO_RESPONSE, TIMOSHENKO_WIG_RESPONSE));
      //"first_name": "Андрей",
        messageResponses.put(178330456l, Arrays.asList(FUCK_YOU_RESPONSE, DRINK_LARD_RESPONSE, BLOW_TIMOSHENKO_RESPONSE));
      //"first_name": "Артём",
        messageResponses.put(301832176l, Arrays.asList(FUCK_YOU_RESPONSE, FUCK_YOU_CLONE_RESPONSE, BLOW_PUTIN_RESPONSE));
      //"first_name": "Goran",
        messageResponses.put(377937124l, Arrays.asList(ALREADY_THERE_RESPONSE));
    }

    @Override
    public void execute() {
        List<Message> messages = vkGateway.getChatMessages(getChatId());
        Collections.reverse(messages);
        for (Message message : messages) {
            if (message.getDate() > lastDate) {
                lastDate = message.getDate();
                //User user = getUser(message.getUserId());
                if (FUCK_YOU_MESSAGE.equals(message.getBody())) {
                    int random = (int)(Math.random() * messageResponses.get(message.getUserId()).size());
                    vkGateway.sendChatMessage(messageResponses.get(message.getUserId()).get(random), getChatId());
                } else if (message.getBody().endsWith(FUCK_SOMEONE_ELSE_MESSAGE) && !FUCK_YOU_MESSAGE.equals(message.getBody())) {
                    vkGateway.sendChatMessage(LIKEWISE_RESPONSE, getChatId());
                }
            }
        }
    }

    private User getUser(long userId) {
        User user = userMap.get(userId);
        if (user == null) {
            user = vkGateway.getUser(userId);
            userMap.put(userId, user);
        }
        return user;
    }

}

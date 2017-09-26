package com.ycm.kata.datacollection.event;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by changmuyu on 2017/9/25.
 * Description:
 */

public class EventManager {
    private static EventManager instance;
    private EventBus eventBus;

    private EventManager() {
        eventBus = EventBus.getDefault();
    }

    public static EventManager GetInstance() {
        if (instance == null) {
            synchronized (EventManager.class) {
                if (instance == null) {
                    instance = new EventManager();
                }
            }
        }
        return instance;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void post(PhotoEvent event) {
        eventBus.post(event);
    }
}

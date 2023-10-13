package net.vakror.jamesconfig.config.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;

public class ConfigEvents {
    //@James usually in a fabric/architectury sense registries are static and non frozen anyways, so the registry wouldnt be event based
    //but instead be just a map you can add yourself to, i am keeping the event wrapper for now
    public static final Event<RegisterEvent> CONFIG_REGISTER_EVENT = EventFactory.createEventResult();
    public static final Event<ObjectRegisterEvent> OBJECT_REGISTER_EVENT = EventFactory.createEventResult();
    public static final Event<RegisterConfigManager> REGISTER_MANAGER = EventFactory.createEventResult();

    public interface RegisterEvent {
        EventResult post(ConfigRegisterEvent event);
    }

    public interface ObjectRegisterEvent {
        EventResult post(ConfigObjectRegisterEvent event);
    }

    public interface RegisterConfigManager{
        EventResult post(RegisterConfigManagersEvent event);
    }
}

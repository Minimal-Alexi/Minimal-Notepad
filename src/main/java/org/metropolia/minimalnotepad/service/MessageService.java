package org.metropolia.minimalnotepad.service;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MessageService {

    private final MessageSource messageSource;

    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String get(String key, Object[] args, Locale locale) {
        return messageSource.getMessage(key, args, locale);
    }

    public String get(String key, Locale locale) {
        return get(key, null, locale);
    }

    public String get(String key) {
        return get(key, null, Locale.getDefault());
    }
}

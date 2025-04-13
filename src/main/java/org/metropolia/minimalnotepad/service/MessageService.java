package org.metropolia.minimalnotepad.service;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * The type Message service.
 */
@Service
public class MessageService {

    private final MessageSource messageSource;

    /**
     * Instantiates a new Message service.
     *
     * @param messageSource the message source
     */
    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Get string.
     *
     * @param key    the key
     * @param args   the args
     * @param locale the locale
     * @return the string
     */
    public String get(String key, Object[] args, Locale locale) {
        return messageSource.getMessage(key, args, locale);
    }

    /**
     * Get string.
     *
     * @param key    the key
     * @param locale the locale
     * @return the string
     */
    public String get(String key, Locale locale) {
        return get(key, null, locale);
    }

    /**
     * Get string.
     *
     * @param key the key
     * @return the string
     */
    public String get(String key) {
        return get(key, null, Locale.getDefault());
    }
}

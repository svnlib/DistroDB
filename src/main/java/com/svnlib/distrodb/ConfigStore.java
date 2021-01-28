package com.svnlib.distrodb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used as a central storage for all configurations associated with the software.
 */
public class ConfigStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigStore.class);

    private static ConfigStore instance;

    public final String DATA_PATH;
    public final Mode   MODE;
    public final String COORDINATOR_HOST;

    private ConfigStore() {
        this.COORDINATOR_HOST = getEnv("DISTRODB_COORDINATOR_HOST", "coordinator");
        this.DATA_PATH = getEnv("DISTRODB_DATA_PATH", "/var/lib/DistroDB/data");
        this.MODE = Mode.from(getEnv("DISTRODB_MODE", "NODE"));
    }

    /**
     * Returns the instance of the {@link ConfigStore} or creates it if not already created.
     *
     * @return the ConfigStore instance
     */
    public static ConfigStore getConfig() {
        if (instance == null) {
            instance = new ConfigStore();
        }
        return instance;
    }

    /**
     * Returns the environment variable with the given key. If the key is null or no variable is associated with it, the
     * default value will be returned.
     *
     * @param key        the key of the environment variable
     * @param defaultVal the default value as fallback
     *
     * @return the value of the environment variable or the fallback
     */
    private static String getEnv(final String key, final String defaultVal) {
        final String value = key != null ? System.getenv(key) : defaultVal;
        return value == null || value.equals("") ? defaultVal : value;
    }

    public enum Mode {
        COORDINATOR,
        PROXY,
        NODE;

        /**
         * Returns the {@link Mode} associated with the name case insensitive. Falls back to Mode.NODE.
         *
         * @param name the name of the mode (case insensitive)
         *
         * @return the named mode or Mode.NODE as fallback.
         */
        public static Mode from(final String name) {
            try {
                return Mode.valueOf(name.toUpperCase());
            } catch (final IllegalArgumentException e) {
                LOGGER.warn("Cannot interpret '{}' as a mode. Defaulting to mode NODE.", name);
                return NODE;
            }
        }
    }

}

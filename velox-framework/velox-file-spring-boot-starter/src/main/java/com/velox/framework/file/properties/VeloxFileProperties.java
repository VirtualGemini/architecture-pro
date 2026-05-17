package com.velox.framework.file.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = VeloxFileProperties.PREFIX)
public class VeloxFileProperties {

    public static final String PREFIX = "velox.file";
    public static final String ENABLED_KEY = "enabled";
    public static final String ENABLED_TRUE = "true";
    public static final String ENABLED_FALSE = "false";

    /**
     * Whether the file capability starter is enabled.
     */
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

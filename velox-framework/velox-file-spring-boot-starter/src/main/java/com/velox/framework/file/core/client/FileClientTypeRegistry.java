package com.velox.framework.file.core.client;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileClientTypeRegistry {

    private final Map<Integer, FileClientTypeRegistration> registrations;

    public FileClientTypeRegistry(Collection<FileClientTypeRegistration> registrations) {
        this.registrations = new LinkedHashMap<>();
        for (FileClientTypeRegistration registration : registrations) {
            this.registrations.put(registration.storage(), registration);
        }
    }

    public FileClientTypeRegistration get(Integer storage) {
        return registrations.get(storage);
    }
}

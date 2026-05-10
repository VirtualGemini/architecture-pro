package com.architecturepro.infrastructure.id;

import com.architecturepro.infrastructure.persistence.IdSequenceMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BusinessIdGenerator {

    private static final int MAX_SEQUENCE = 9999999;

    private final IdSequenceMapper idSequenceMapper;

    public BusinessIdGenerator(IdSequenceMapper idSequenceMapper) {
        this.idSequenceMapper = idSequenceMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public String nextUserId() {
        return next("user", "U");
    }

    @Transactional(rollbackFor = Exception.class)
    public String nextRoleId() {
        return next("role", "R");
    }

    @Transactional(rollbackFor = Exception.class)
    public String nextMenuId() {
        return next("menu", "M");
    }

    @Transactional(rollbackFor = Exception.class)
    public String nextProfileId() {
        return next("profile", "F");
    }

    @Transactional(rollbackFor = Exception.class)
    public String nextUserRoleId() {
        return next("user_role", "L");
    }

    @Transactional(rollbackFor = Exception.class)
    public String nextRoleMenuPermissionId() {
        return next("role_menu_permission", "P");
    }

    @Transactional(rollbackFor = Exception.class)
    public String nextFileConfigId() {
        return next("file_config", "C");
    }

    @Transactional(rollbackFor = Exception.class)
    public String nextFileId() {
        return next("file", "F");
    }

    @Transactional(rollbackFor = Exception.class)
    public String nextFileContentId() {
        return next("file_content", "D");
    }

    private String next(String bizCode, String prefix) {
        Integer currentValue = idSequenceMapper.selectCurrentValueForUpdate(bizCode);
        int nextValue = currentValue == null ? 1 : currentValue + 1;
        if (nextValue > MAX_SEQUENCE) {
            throw new IllegalStateException("ID sequence overflow for bizCode=" + bizCode);
        }
        if (currentValue == null) {
            idSequenceMapper.insert(bizCode, nextValue);
        } else {
            idSequenceMapper.update(bizCode, nextValue);
        }
        return prefix + String.format("%07d", nextValue);
    }
}

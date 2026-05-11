package com.architecturepro.infrastructure.web.permission.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.architecturepro.common.constants.SystemRoleCode;
import com.architecturepro.common.exception.ApiException;
import com.architecturepro.common.exception.BusinessErrorCode;
import com.architecturepro.domain.model.Menu;
import com.architecturepro.domain.model.Role;
import com.architecturepro.domain.model.RoleMenuPermission;
import com.architecturepro.domain.model.UserRole;
import com.architecturepro.infrastructure.id.BusinessIdGenerator;
import com.architecturepro.infrastructure.persistence.MenuMapper;
import com.architecturepro.infrastructure.persistence.RoleMapper;
import com.architecturepro.infrastructure.persistence.RoleMenuPermissionMapper;
import com.architecturepro.infrastructure.persistence.UserRoleMapper;
import com.architecturepro.infrastructure.persistence.support.MenuQuerySupport;
import com.architecturepro.infrastructure.web.permission.service.PermissionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final RoleMapper roleMapper;
    private final MenuMapper menuMapper;
    private final RoleMenuPermissionMapper roleMenuPermissionMapper;
    private final UserRoleMapper userRoleMapper;
    private final BusinessIdGenerator businessIdGenerator;

    public PermissionServiceImpl(RoleMapper roleMapper,
                                 MenuMapper menuMapper,
                                 RoleMenuPermissionMapper roleMenuPermissionMapper,
                                 UserRoleMapper userRoleMapper,
                                 BusinessIdGenerator businessIdGenerator) {
        this.roleMapper = roleMapper;
        this.menuMapper = menuMapper;
        this.roleMenuPermissionMapper = roleMenuPermissionMapper;
        this.userRoleMapper = userRoleMapper;
        this.businessIdGenerator = businessIdGenerator;
    }

    @Override
    public Set<String> getRoleMenuIds(String roleId) {
        return getRoleMenuIds(Set.of(roleId));
    }

    @Override
    public Set<String> getRoleMenuIds(Collection<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Set.of();
        }
        return roleMenuPermissionMapper.selectList(new LambdaQueryWrapper<RoleMenuPermission>()
                        .eq(RoleMenuPermission::getDeleted, 0)
                        .in(RoleMenuPermission::getRoleId, roleIds))
                .stream()
                .map(RoleMenuPermission::getMenuId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoleMenu(String roleId, Collection<String> menuIds) {
        Role role = roleMapper.selectById(roleId);
        if (role == null || Integer.valueOf(1).equals(role.getDeleted())) {
            throw new ApiException(BusinessErrorCode.ROLE_NOT_FOUND);
        }

        Set<String> requestedMenuIds = menuIds == null
                ? Set.of()
                : menuIds.stream().filter(Objects::nonNull).collect(Collectors.toCollection(LinkedHashSet::new));

        Map<String, RoleMenuPermission> activePermissionMap = new LinkedHashMap<>();
        Map<String, RoleMenuPermission> deletedPermissionMap = new LinkedHashMap<>();
        for (RoleMenuPermission permission : roleMenuPermissionMapper.selectAllByRoleId(roleId)) {
            String menuId = permission.getMenuId();
            if (menuId == null) {
                continue;
            }
            if (Integer.valueOf(1).equals(permission.getDeleted())) {
                deletedPermissionMap.merge(menuId, permission, this::preferLatestPermission);
                continue;
            }
            activePermissionMap.merge(menuId, permission, this::preferLatestPermission);
        }
        Set<String> dbMenuIds = new LinkedHashSet<>(activePermissionMap.keySet());

        Set<String> validMenuIds = requestedMenuIds.isEmpty() ? Set.of() : menuMapper.selectList(MenuQuerySupport.selectColumns(new LambdaQueryWrapper<Menu>())
                        .eq(Menu::getDeleted, 0)
                        .eq(Menu::getIsEnable, 1)
                        .in(Menu::getId, requestedMenuIds))
                .stream()
                .map(Menu::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<String> createIds = new LinkedHashSet<>(validMenuIds);
        createIds.removeAll(dbMenuIds);

        Set<String> deleteIds = new LinkedHashSet<>(dbMenuIds);
        deleteIds.removeAll(validMenuIds);

        String operator = currentOperator();
        if (!deleteIds.isEmpty()) {
            List<String> deleteRelationIds = deleteIds.stream()
                    .map(activePermissionMap::get)
                    .filter(Objects::nonNull)
                    .map(RoleMenuPermission::getId)
                    .filter(Objects::nonNull)
                    .toList();
            if (!deleteRelationIds.isEmpty()) {
                roleMenuPermissionMapper.logicalDeleteByIds(deleteRelationIds, operator);
            }
        }

        List<String> restoreRelationIds = createIds.stream()
                .map(deletedPermissionMap::get)
                .filter(Objects::nonNull)
                .map(RoleMenuPermission::getId)
                .filter(Objects::nonNull)
                .toList();
        if (!restoreRelationIds.isEmpty()) {
            roleMenuPermissionMapper.restoreByIds(restoreRelationIds, operator);
            createIds.removeIf(deletedPermissionMap::containsKey);
        }

        if (!createIds.isEmpty()) {
            List<RoleMenuPermission> entities = new ArrayList<>(createIds.size());
            for (String menuId : createIds) {
                RoleMenuPermission permission = new RoleMenuPermission();
                permission.setId(businessIdGenerator.nextRoleMenuPermissionId());
                permission.setRoleId(roleId);
                permission.setMenuId(menuId);
                permission.setDeleted(0);
                permission.setCreateBy(operator);
                permission.setUpdateBy(operator);
                entities.add(permission);
            }
            for (RoleMenuPermission entity : entities) {
                roleMenuPermissionMapper.insert(entity);
            }
        }
    }

    @Override
    public Set<String> getUserRoleIds(String userId) {
        return getUserRoleIds(Set.of(userId)).getOrDefault(userId, Set.of());
    }

    @Override
    public Map<String, Set<String>> getUserRoleIds(Collection<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Map.of();
        }
        Map<String, Set<String>> userRoles = new LinkedHashMap<>();
        userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>()
                        .eq(UserRole::getDeleted, 0)
                        .in(UserRole::getUserId, userIds))
                .forEach(item -> userRoles.computeIfAbsent(item.getUserId(), key -> new LinkedHashSet<>()).add(item.getRoleId()));
        return userRoles;
    }

    @Override
    public List<String> getUserRoleCodes(String userId) {
        return getUserRoleCodes(Set.of(userId)).getOrDefault(userId, List.of());
    }

    @Override
    public Map<String, List<String>> getUserRoleCodes(Collection<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Map.of();
        }
        Map<String, Set<String>> userRoleIdMap = getUserRoleIds(userIds);
        Set<String> allRoleIds = userRoleIdMap.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (allRoleIds.isEmpty()) {
            return Map.of();
        }

        Map<String, String> roleCodeMap = roleMapper.selectList(new LambdaQueryWrapper<Role>()
                        .eq(Role::getDeleted, 0)
                        .in(Role::getId, allRoleIds))
                .stream()
                .filter(role -> role.getRoleCode() != null && !role.getRoleCode().isBlank())
                .collect(Collectors.toMap(Role::getId, Role::getRoleCode, (left, right) -> left, LinkedHashMap::new));

        Map<String, List<String>> result = new LinkedHashMap<>();
        for (Map.Entry<String, Set<String>> entry : userRoleIdMap.entrySet()) {
            List<String> roleCodes = entry.getValue().stream()
                    .map(roleCodeMap::get)
                    .filter(Objects::nonNull)
                    .toList();
            result.put(entry.getKey(), roleCodes);
        }
        return result;
    }

    @Override
    public Integer getUserHighestRoleLevel(String userId) {
        return getUserHighestRoleLevels(Set.of(userId)).getOrDefault(userId, 0);
    }

    @Override
    public Map<String, Integer> getUserHighestRoleLevels(Collection<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Map.of();
        }

        Map<String, Set<String>> userRoleIdMap = getUserRoleIds(userIds);
        Set<String> allRoleIds = userRoleIdMap.values().stream()
                .flatMap(Set::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Map<String, Integer> roleLevelMap = allRoleIds.isEmpty()
                ? Map.of()
                : roleMapper.selectList(new LambdaQueryWrapper<Role>()
                                .eq(Role::getDeleted, 0)
                                .in(Role::getId, allRoleIds))
                        .stream()
                        .collect(Collectors.toMap(
                                Role::getId,
                                role -> role.getRoleLevel() == null ? 0 : role.getRoleLevel(),
                                (left, right) -> left,
                                LinkedHashMap::new
                        ));

        Map<String, Integer> result = new LinkedHashMap<>();
        for (String userId : userIds) {
            int highestLevel = userRoleIdMap.getOrDefault(userId, Set.of()).stream()
                    .map(roleLevelMap::get)
                    .filter(Objects::nonNull)
                    .max(Integer::compareTo)
                    .orElse(0);
            result.put(userId, highestLevel);
        }
        return result;
    }

    @Override
    public List<String> getUserPermissionMarks(String userId) {
        if (userId == null || userId.isBlank()) {
            return List.of();
        }

        boolean isSuperAdmin = getUserRoleCodes(userId).stream().anyMatch(SystemRoleCode.R_SUPER::equals);
        LambdaQueryWrapper<Menu> wrapper = MenuQuerySupport.selectColumns(new LambdaQueryWrapper<Menu>())
                .eq(Menu::getDeleted, 0)
                .eq(Menu::getIsEnable, 1);

        if (!isSuperAdmin) {
            Set<String> roleIds = getUserRoleIds(userId);
            if (roleIds.isEmpty()) {
                return List.of();
            }

            Set<String> menuIds = getRoleMenuIds(roleIds);
            if (menuIds.isEmpty()) {
                return List.of();
            }
            wrapper.in(Menu::getId, menuIds);
        }

        return menuMapper.selectList(wrapper).stream()
                .map(Menu::getAuthMark)
                .filter(mark -> mark != null && !mark.isBlank())
                .distinct()
                .toList();
    }

    private RoleMenuPermission preferLatestPermission(RoleMenuPermission left, RoleMenuPermission right) {
        String leftId = left.getId();
        String rightId = right.getId();
        if (leftId == null) {
            return right;
        }
        if (rightId == null) {
            return left;
        }
        return rightId.compareTo(leftId) > 0 ? right : left;
    }

    private String currentOperator() {
        return StpUtil.isLogin() ? String.valueOf(StpUtil.getLoginIdDefaultNull()) : "system";
    }
}

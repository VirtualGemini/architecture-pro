package com.velox.infrastructure.web.menu.service;

import com.velox.infrastructure.web.menu.dto.MenuSaveCommand;
import com.velox.infrastructure.web.menu.dto.MenuRouteDTO;

import java.util.List;

public interface MenuService {

    List<MenuRouteDTO> getSimpleMenus();

    String create(MenuSaveCommand command);

    Boolean update(String menuId, MenuSaveCommand command);

    Boolean delete(String menuId);
}

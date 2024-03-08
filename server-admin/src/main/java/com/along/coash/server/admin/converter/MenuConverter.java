package com.along.coash.server.admin.converter;


import com.along.coash.server.admin.contract.menu.Menu;
import com.along.coash.server.admin.contract.menu.MenuCreateRequest;
import com.along.coash.server.admin.contract.menu.MenuSimple;
import com.along.coash.server.admin.contract.menu.MenuUpdateRequest;
import com.along.coash.server.admin.entities.MenuDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MenuConverter {
    MenuConverter INSTANCE = Mappers.getMapper(MenuConverter.class);

    List<Menu> convert(List<MenuDO> list);

    MenuDO convert(MenuCreateRequest bean);

    MenuDO convert(MenuUpdateRequest bean);

    Menu convert(MenuDO bean);

    List<MenuSimple> convertToSimple(List<MenuDO> list);

    MenuSimple menuDOToMenuSimpleRespVO(MenuDO menuDO);
}

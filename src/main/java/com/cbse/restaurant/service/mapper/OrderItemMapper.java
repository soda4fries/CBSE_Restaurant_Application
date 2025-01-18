package com.cbse.restaurant.service.mapper;

import com.cbse.restaurant.domain.MenuItem;
import com.cbse.restaurant.domain.Order;
import com.cbse.restaurant.domain.OrderItem;
import com.cbse.restaurant.service.dto.MenuItemDTO;
import com.cbse.restaurant.service.dto.OrderDTO;
import com.cbse.restaurant.service.dto.OrderItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(target = "menuItem", source = "menuItem", qualifiedByName = "menuItemName")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    OrderItemDTO toDto(OrderItem s);

    @Named("menuItemName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    MenuItemDTO toDtoMenuItemName(MenuItem menuItem);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderDTO toDtoOrderId(Order order);
}

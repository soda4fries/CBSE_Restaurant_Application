package com.cbse.restaurant.service.mapper;

import com.cbse.restaurant.domain.Staff;
import com.cbse.restaurant.service.dto.StaffDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Staff} and its DTO {@link StaffDTO}.
 */
@Mapper(componentModel = "spring")
public interface StaffMapper extends EntityMapper<StaffDTO, Staff> {
    @Mapping(target = "manager", source = "manager", qualifiedByName = "staffId")
    StaffDTO toDto(Staff s);

    @Named("staffId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StaffDTO toDtoStaffId(Staff staff);
}

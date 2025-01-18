package com.cbse.restaurant.service.mapper;

import com.cbse.restaurant.domain.Customer;
import com.cbse.restaurant.domain.Reservation;
import com.cbse.restaurant.service.dto.CustomerDTO;
import com.cbse.restaurant.service.dto.ReservationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reservation} and its DTO {@link ReservationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReservationMapper extends EntityMapper<ReservationDTO, Reservation> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    ReservationDTO toDto(Reservation s);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);
}

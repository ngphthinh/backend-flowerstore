package com.ngphthinh.flower.mapper;

import com.ngphthinh.flower.dto.request.StoreRequest;
import com.ngphthinh.flower.dto.response.StoreResponse;
import com.ngphthinh.flower.entity.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StoreMapper {

//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "id", ignore = true)
    Store toStore(StoreRequest request);

    StoreResponse toStoreResponse(Store store);

}

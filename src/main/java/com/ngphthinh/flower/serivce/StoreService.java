package com.ngphthinh.flower.serivce;

import com.ngphthinh.flower.dto.request.StoreRequest;
import com.ngphthinh.flower.dto.response.StoreResponse;
import com.ngphthinh.flower.dto.response.StoreStatisticsResponse;
import com.ngphthinh.flower.entity.Store;
import com.ngphthinh.flower.exception.AppException;
import com.ngphthinh.flower.exception.ErrorCode;
import com.ngphthinh.flower.mapper.StoreMapper;
import com.ngphthinh.flower.repo.StoreRepository;
import com.ngphthinh.flower.util.DateRange;
import com.ngphthinh.flower.util.StatisticsUtil;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    public StoreService(StoreRepository storeRepository, StoreMapper storeMapper) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
    }


    @PostAuthorize("hasRole('ADMIN')")
    public StoreResponse createStore(StoreRequest request) {
        Store store = storeMapper.toStore(request);
        return storeMapper.toStoreResponse(storeRepository.save(store));
    }

    @PostAuthorize("hasRole('ADMIN')")
    public StoreResponse getStoreById(Long id) {
        return storeMapper.toStoreResponse(getStoreEntityById(id));
    }


    @PostAuthorize("hasAuthority('VIEW_STORE')")
    public List<StoreResponse> getAllStore() {
        return storeRepository.findAll().stream().map(storeMapper::toStoreResponse).toList();
    }

    @PostAuthorize("hasRole('ADMIN')")
    public StoreResponse updateStore(Long id, StoreRequest request) {
        Store store = getStoreEntityById(id);
        storeMapper.updateStoreFormRequest(request, store);
        return storeMapper.toStoreResponse(storeRepository.save(store));
    }

    @PostAuthorize("hasRole('ADMIN')")
    public StoreResponse deleteById(Long id) {
        Store store = getStoreEntityById(id);
        storeRepository.deleteById(id);
        return storeMapper.toStoreResponse(store);
    }

    public Store getStoreEntityById(Long id) {
        if (id == null) {
            throw new AppException(ErrorCode.STORE_ID_NULL);
        }
        return storeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STORE_NOT_FOUND, "id", id.toString()));
    }

    public boolean isStoreExist(Long id) {
        return storeRepository.existsById(id);
    }

    public List<StoreStatisticsResponse> getStoreStatistics(String stateStatisticsDay) {

        DateRange dateRange = StatisticsUtil.getDateRange(stateStatisticsDay);

        var storeStatistics = storeRepository.getStoreStatistics(dateRange.getStartDate(), dateRange.getEndDate());
        if (storeStatistics.isEmpty()) {
            return storeRepository.findAll().stream().map(
                    store -> new StoreStatisticsResponse(
                            store.getId(),
                            store.getName()
                    )
            ).toList();
        } else {
            return storeStatistics;
        }
    }
    public StoreStatisticsResponse getStoreStatisticsAll(String stateStatisticsDay) {

        DateRange dateRange = StatisticsUtil.getDateRange(stateStatisticsDay);
        return storeRepository.getStoreStatisticsAll(dateRange.getStartDate(), dateRange.getEndDate());

    }



}

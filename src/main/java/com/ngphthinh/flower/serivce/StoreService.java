package com.ngphthinh.flower.serivce;

import com.ngphthinh.flower.dto.request.StoreRequest;
import com.ngphthinh.flower.dto.response.StoreResponse;
import com.ngphthinh.flower.dto.response.StoreStatisticsResponse;
import com.ngphthinh.flower.entity.Store;
import com.ngphthinh.flower.exception.AppException;
import com.ngphthinh.flower.exception.ErrorCode;
import com.ngphthinh.flower.mapper.StoreMapper;
import com.ngphthinh.flower.repo.StoreRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

        LocalDateTime startDate;
        LocalDateTime endDate;


        switch (stateStatisticsDay) {
            case "TODAY" -> {
                startDate = LocalDate.now().atStartOfDay();
                endDate = LocalDate.now().atTime(LocalTime.MAX);
            }
            case "DAY_7" -> {
                startDate = LocalDate.now().minusDays(7).atStartOfDay();
                endDate = LocalDate.now().atTime(LocalTime.MAX);
            }
            case "DAY_30" -> {
                startDate = LocalDate.now().minusDays(30).atStartOfDay();
                endDate = LocalDate.now().atTime(LocalTime.MAX);
            }
            case "DAY_365" -> {
                startDate = LocalDate.now().minusYears(1).atStartOfDay();
                endDate = LocalDate.now().atTime(LocalTime.MAX);
            }
            default ->
                    throw new AppException(ErrorCode.INVALID_STATE_STATISTICS_DAY, "stateStatisticsDay", stateStatisticsDay);

        }

        var storeStatistics = storeRepository.getStoreStatistics(startDate, endDate);
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

        LocalDateTime startDate;
        LocalDateTime endDate;


        switch (stateStatisticsDay) {
            case "TODAY" -> {
                startDate = LocalDate.now().atStartOfDay();
                endDate = LocalDate.now().atTime(LocalTime.MAX);
            }
            case "DAY_7" -> {
                startDate = LocalDate.now().minusDays(7).atStartOfDay();
                endDate = LocalDate.now().atTime(LocalTime.MAX);
            }
            case "DAY_30" -> {
                startDate = LocalDate.now().minusDays(30).atStartOfDay();
                endDate = LocalDate.now().atTime(LocalTime.MAX);
            }
            case "DAY_365" -> {
                startDate = LocalDate.now().minusYears(1).atStartOfDay();
                endDate = LocalDate.now().atTime(LocalTime.MAX);
            }
            default ->
                    throw new AppException(ErrorCode.INVALID_STATE_STATISTICS_DAY, "stateStatisticsDay", stateStatisticsDay);

        }

        return storeRepository.getStoreStatisticsAll(startDate, endDate);

    }



}

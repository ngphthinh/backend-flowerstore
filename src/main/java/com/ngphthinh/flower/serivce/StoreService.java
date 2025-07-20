package com.ngphthinh.flower.serivce;

import com.ngphthinh.flower.dto.request.StoreRequest;
import com.ngphthinh.flower.dto.response.StoreResponse;
import com.ngphthinh.flower.entity.Store;
import com.ngphthinh.flower.exception.AppException;
import com.ngphthinh.flower.exception.ErrorCode;
import com.ngphthinh.flower.mapper.StoreMapper;
import com.ngphthinh.flower.repo.StoreRepository;
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


    public StoreResponse createStore(StoreRequest request) {
        Store store = storeMapper.toStore(request);
        return storeMapper.toStoreResponse(storeRepository.save(store));
    }

    public StoreResponse getStoreById(Long id) {
        return storeMapper.toStoreResponse(getStoreEntityById(id));
    }

    public List<StoreResponse> getAllStore() {
        return storeRepository.findAll().stream().map(storeMapper::toStoreResponse).toList();
    }

    public StoreResponse updateStore(Long id, StoreRequest request) {
        Store store = getStoreEntityById(id);
        storeMapper.updateStoreFormRequest(request, store);
        return storeMapper.toStoreResponse(storeRepository.save(store));
    }

    public StoreResponse deleteById(Long id) {
        Store store = getStoreEntityById(id);
        storeRepository.deleteById(id);
        return storeMapper.toStoreResponse(store);
    }

    public Store getStoreEntityById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STORE_NOT_FOUND));
    }
}

package com._3line.gravity.freedom.mediaintegration.service;

import com._3line.gravity.freedom.mediaintegration.dto.AppDeviceDTO;
import com._3line.gravity.freedom.mediaintegration.model.AppDevice;

import java.util.List;

public interface AppDeviceService {

    AppDeviceDTO saveMediaType(AppDeviceDTO mediaTypeDTO);

    AppDeviceDTO updateMediaType(AppDeviceDTO mediaTypeDTO);

    List<AppDeviceDTO> fetchMediaTypes();

    AppDeviceDTO findByMediaType(String mediaType);

    AppDeviceDTO findByMediaId(Long id);
}

package com.dia.dia_be.service.pb.intf;

import java.util.List;

import com.dia.dia_be.dto.pb.productDTO.ResponseProductDTO;

public interface PbProductService {
	List<ResponseProductDTO> getProducts(String tag);
}

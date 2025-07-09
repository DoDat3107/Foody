package com.codegym.foody.repository;

import com.codegym.foody.model.Voucher;
import com.codegym.foody.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IVoucherService extends IGeneralService<Voucher> {
    Page<Voucher> findWithPaginationAndKeyword(String keyword, Pageable pageable);
}

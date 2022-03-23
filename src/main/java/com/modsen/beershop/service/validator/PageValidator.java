package com.modsen.beershop.service.validator;

import com.modsen.beershop.repository.config.Configuration;
import com.modsen.beershop.service.exceprion.ValidateException;

public enum PageValidator {
    INSTANCE;

    public static final String INVALID_PAGE = "Invalid page";

    public void validatePage(Integer page) {
        if (page <= 0) {
            throw new ValidateException(INVALID_PAGE);
        }
    }

    public Integer validatePageSize(Integer pageSize) {
        final Integer pageSizeMin = Configuration.INSTANCE.getPageSizeMin();
        final Integer pageSizeMax = Configuration.INSTANCE.getPageSizeMax();

        if (pageSize < pageSizeMin) {
            return pageSizeMin;
        }
        if (pageSize > pageSizeMax) {
            return pageSizeMax;
        }
        return pageSize;
    }
}

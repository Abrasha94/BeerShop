package com.modsen.beershop.service.validator;

import com.modsen.beershop.config.Configuration;
import com.modsen.beershop.config.Messages;
import com.modsen.beershop.service.exception.ValidateException;

public enum PageValidator {
    INSTANCE;

    public void validatePage(Integer page) {
        if (page <= 0) {
            throw new ValidateException(Messages.MESSAGE.invalidPage());
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

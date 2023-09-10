package me.boot.base.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class MultiResult<T> extends Result {

    private Collection<T> data;

    public List<T> getData() {
        if (null == data) {
            return Collections.emptyList();
        }
        if (data instanceof List) {
            return (List<T>) data;
        }
        return new ArrayList<>(data);
    }

    public void setData(Collection<T> data) {
        this.data = data;
    }

    public boolean isEmpty() {
        return data == null || data.isEmpty();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public static MultiResult success() {
        return success(Collections.emptyList());
    }

    public static MultiResult failure(String errCode, String errMessage) {
        MultiResult response = new MultiResult();
        response.setSuccess(false);
        response.setErrCode(errCode);
        response.setErrMessage(errMessage);
        return response;
    }

    public static <T> MultiResult<T> success(Collection<T> data) {
        MultiResult<T> response = new MultiResult<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

}

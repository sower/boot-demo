package me.boot.base.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 分页结果
 */
public class PageResult<T> extends Result {

    private int totalCount = 0;

    private int pageSize = 1;

    private int pageIndex = 1;

    private Collection<T> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = Math.max(pageSize, 1);
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = Math.max(pageIndex, 1);
    }

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

    public int getTotalPages() {
        return this.totalCount % this.pageSize == 0 ? this.totalCount
            / this.pageSize : (this.totalCount / this.pageSize) + 1;
    }

    public boolean isEmpty() {
        return data == null || data.isEmpty();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public static PageResult success() {
        return success(1, 1);
    }

    public static PageResult failure(String errCode, String errMessage) {
        PageResult response = new PageResult();
        response.setSuccess(false);
        response.setErrCode(errCode);
        response.setErrMessage(errMessage);
        return response;
    }

    public static <T> PageResult<T> success(int pageSize, int pageIndex) {
        return success(Collections.emptyList(), 0, pageSize, pageIndex);
    }

    public static <T> PageResult<T> success(Collection<T> data, int totalCount, int pageSize,
        int pageIndex) {
        PageResult<T> response = new PageResult<>();
        response.setSuccess(true);
        response.setData(data);
        response.setTotalCount(totalCount);
        response.setPageSize(pageSize);
        response.setPageIndex(pageIndex);
        return response;
    }

}

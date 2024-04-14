package me.boot.base.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分页结果
 */
@ToString
public class PageResult<T> extends Result {

    @Setter
    @Getter
    private int totalCount = 0;

    @Getter
    private int pageSize = 1;

    @Getter
    private int pageIndex = 1;

    @Setter
    private Collection<T> data;

    public void setPageSize(int pageSize) {
        this.pageSize = Math.max(pageSize, 1);
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

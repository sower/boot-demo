package me.boot.base.dto;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @date 2022/09/18
 */
public class SingleResult<T> extends Result {


    // 数据体
    @Getter
    @Setter
    private T data;


    public static <T> SingleResult<T> success(T data) {
        SingleResult<T> response = new SingleResult<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

    public static SingleResult success() {
        return success(null);
    }

    public static SingleResult failure(String errMessage) {
        return failure("ValidError", errMessage);
    }

    public static SingleResult failure(String errCode, String errMessage) {
        SingleResult response = new SingleResult<>();
        response.setSuccess(false);
        response.setErrCode(errCode);
        response.setErrMessage(errMessage);
        return response;
    }


}

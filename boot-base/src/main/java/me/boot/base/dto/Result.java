package me.boot.base.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Result {

    private boolean success;

    private String errCode;

    private String errMessage;

    public static Result success() {
        Result response = new Result();
        response.setSuccess(true);
        return response;
    }

    public static Result failure(String errCode, String errMessage) {
        Result response = new Result();
        response.setSuccess(false);
        response.setErrCode(errCode);
        response.setErrMessage(errMessage);
        return response;
    }

}

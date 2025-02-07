package com.example.demo.exception;

import com.example.demo.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 */
@ControllerAdvice("com.example.demo.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.error();
    }

    //自定义的异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result error(CustomException e) {
        e.printStackTrace();
        return Result.error(e.getCode(), e.getMsg());
    }
}

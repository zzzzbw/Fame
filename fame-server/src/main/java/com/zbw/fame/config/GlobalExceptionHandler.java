package com.zbw.fame.config;

import com.zbw.fame.exception.LoginExpireException;
import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.exception.NotLoginException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.util.ErrorCode;
import com.zbw.fame.util.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


/*
  常见异常处理并返回相应错误码
  SpringMVC自定义异常对应的status code
  Exception                               HTTP Status Code
  ConversionNotSupportedException         500 (Internal Server Error)
  HttpMessageNotWritableException         500 (Internal Server Error)
  HttpMediaTypeNotSupportedException      415 (Unsupported Media Type)
  HttpMediaTypeNotAcceptableException     406 (Not Acceptable)
  HttpRequestMethodNotSupportedException  405 (Method Not Allowed)
  NoHandlerFoundException                 404 (Not Found)
  TypeMismatchException                   400 (Bad Request)
  HttpMessageNotReadableException         400 (Bad Request)
  MissingServletRequestParameterException 400 (Bad Request)
  MethodArgumentNotValidException         400 (Bad Request)
  BindException                           400 (Bad Request)
  ConstraintViolationException            400 (Bad Request)
 */

/**
 * 全局异常处理 Controller
 *
 * @author zzzzbw
 * @since 2017/8/30 12:25
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Tip异常返回
     *
     * @param req {@link HttpServletRequest}
     * @param e   {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(value = TipException.class)
    public RestResponse<RestResponse.Empty> tipErrorHandler(HttpServletRequest req, TipException e) {
        return RestResponse.fail(e.getMessage());
    }

    /**
     * NotFound异常返回
     *
     * @param req {@link HttpServletRequest}
     * @param e   {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(value = NotFoundException.class)
    public RestResponse<RestResponse.Empty> NotFoundErrorHandler(HttpServletRequest req, NotFoundException e) {
        String message = "";
        if (null != e.getClz()) {
            message = e.getClz().getSimpleName();
        }
        message += "资源不存在!";
        return RestResponse.fail(ErrorCode.NOT_FOUND.getCode(), message);
    }

    /**
     * NotLogin异常返回
     *
     * @param req {@link HttpServletRequest}
     * @param e   {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(value = NotLoginException.class)
    public RestResponse<RestResponse.Empty> NotLoginErrorHandler(HttpServletRequest req, HttpServletResponse rep, NotLoginException e) {
        return RestResponse.fail(ErrorCode.NOT_LOGIN.getCode(), ErrorCode.NOT_LOGIN.getMsg());
    }

    /**
     * LoginExpire异常处理
     *
     * @param req
     * @param rep
     * @param e
     * @return
     */
    @ExceptionHandler(value = LoginExpireException.class)
    public RestResponse<RestResponse.Empty> LoginExpireErrorHandler(HttpServletRequest req, HttpServletResponse rep, LoginExpireException e) {
        return RestResponse.fail(ErrorCode.LOGIN_EXPIRE.getCode(), ErrorCode.LOGIN_EXPIRE.getMsg());
    }

    /**
     * 运行时异常
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param re  {@link RuntimeException}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(RuntimeException.class)
    public RestResponse<RestResponse.Empty> runtimeExceptionHandler(HttpServletRequest req, HttpServletResponse rep, RuntimeException re) {
        log.error("---RuntimeException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), re.getMessage(), re);
        return RestResponse.fail(ErrorCode.RUNTIME.getCode(), ErrorCode.RUNTIME.getMsg());
    }

    /**
     * 空指针异常
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link NullPointerException}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(NullPointerException.class)
    public RestResponse<RestResponse.Empty> nullPointerExceptionHandler(HttpServletRequest req, HttpServletResponse rep, NullPointerException ex) {
        log.error("---NullPointerException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        return RestResponse.fail(ErrorCode.NULL_POINTER.getCode(), ErrorCode.NULL_POINTER.getMsg());
    }

    /**
     * 类型转换异常
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link ClassCastException}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(ClassCastException.class)
    public RestResponse<RestResponse.Empty> classCastExceptionHandler(HttpServletRequest req, HttpServletResponse rep, ClassCastException ex) {
        log.error("---classCastException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        return RestResponse.fail(ErrorCode.CLASS_CAST.getCode(), ErrorCode.CLASS_CAST.getMsg());
    }

    /**
     * IO异常
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link IOException}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(IOException.class)
    public RestResponse<RestResponse.Empty> classCastExceptionHandler(HttpServletRequest req, HttpServletResponse rep, IOException ex) {
        log.error("---classCastException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        return RestResponse.fail(ErrorCode.IO.getCode(), ErrorCode.IO.getMsg());
    }

    /**
     * 未知方法异常
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link NoSuchMethodException}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(NoSuchMethodException.class)
    public RestResponse<RestResponse.Empty> noSuchMethodExceptionHandler(HttpServletRequest req, HttpServletResponse rep, NoSuchMethodException ex) {
        log.error("---noSuchMethodException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        return RestResponse.fail(ErrorCode.NO_SUCH_METHOD.getCode(), ErrorCode.NO_SUCH_METHOD.getMsg());
    }

    /**
     * 数组越界异常
     *
     * @param req {@link HttpServletRequest}
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link IndexOutOfBoundsException}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public RestResponse<RestResponse.Empty> indexOutOfBoundsExceptionHandler(HttpServletRequest req, HttpServletResponse rep, IndexOutOfBoundsException ex) {
        log.error("---indexOutOfBoundsException Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        return RestResponse.fail(ErrorCode.INDEX_OUTOF_BOUNDS.getCode(), ErrorCode.INDEX_OUTOF_BOUNDS.getMsg());
    }

    /**
     * 400相关异常
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler({HttpMessageNotReadableException.class, TypeMismatchException.class, MissingServletRequestParameterException.class})
    public RestResponse<RestResponse.Empty> request400(HttpServletRequest req, HttpServletResponse rep, Exception ex) {
        log.error("---request400 Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        return RestResponse.fail(ErrorCode.BAD_REQUEST.getCode(), ErrorCode.BAD_REQUEST.getMsg());
    }

    /**
     * {@link RequestBody} 参数绑定对象时候抛出的异常
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public RestResponse<RestResponse.Empty> methodArgumentNotValidExceptionHandler(HttpServletRequest req, HttpServletResponse rep, MethodArgumentNotValidException ex) {
        log.error("---methodArgumentNotValidExceptionHandler Handler---Host {}, invokes url {},  ERROR: {}",
                req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);

        List<String> defaultMsg = ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        return RestResponse.fail(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getCode(), defaultMsg.get(0));
    }


    /**
     * 参数绑定对象时候抛出的异常
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(value = BindException.class)
    public RestResponse<RestResponse.Empty> handleBindException(HttpServletRequest req, HttpServletResponse rep, BindException ex) {
        log.error("---methodArgumentNotValidExceptionHandler Handler---Host {}, invokes url {},  ERROR: {}",
                req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);

        List<String> defaultMsg = ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        return RestResponse.fail(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getCode(), defaultMsg.get(0));
    }

    /**
     * 单个参数校验
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public RestResponse<RestResponse.Empty> handleBindGetException(HttpServletRequest req, HttpServletResponse rep, ConstraintViolationException ex) {
        log.error("---methodArgumentNotValidExceptionHandler Handler---Host {}, invokes url {},  ERROR: {}",
                req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);

        List<String> defaultMsg = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return RestResponse.fail(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getCode(), defaultMsg.get(0));
    }

    /**
     * 404异常
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public RestResponse<RestResponse.Empty> request404(HttpServletRequest req, HttpServletResponse rep, Exception ex) {
        log.error("---request404 Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        return RestResponse.fail(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMsg());
    }

    /**
     * 405相关异常
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public RestResponse<RestResponse.Empty> request405(HttpServletRequest req, HttpServletResponse rep, Exception ex) {
        log.error("---request405 Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        return RestResponse.fail(ErrorCode.METHOD_BOT_ALLOWED.getCode(), ErrorCode.METHOD_BOT_ALLOWED.getMsg());
    }

    /**
     * 406相关异常
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public RestResponse<RestResponse.Empty> request406(HttpServletRequest req, HttpServletResponse rep, Exception ex) {
        log.error("---request406 Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        return RestResponse.fail(ErrorCode.NOT_ACCEPTABLE.getCode(), ErrorCode.NOT_ACCEPTABLE.getMsg());
    }

    /**
     * 500相关异常
     *
     * @param rep {@link HttpServletResponse}
     * @param ex  {@link Exception}
     * @return {@link RestResponse}
     */
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    public RestResponse<RestResponse.Empty> server500(HttpServletRequest req, HttpServletResponse rep, Exception ex) {
        log.error("---server500 Handler---Host {}, invokes url {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), ex.getMessage(), ex);
        return RestResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), ErrorCode.INTERNAL_SERVER_ERROR.getMsg());
    }

    /**
     * 全局异常返回
     *
     * @param req {@link HttpServletRequest}
     * @param e   {@link HttpServletResponse}
     * @return {@link Exception}
     */
    @ExceptionHandler(value = Exception.class)
    public RestResponse<RestResponse.Empty> defaultErrorHandler(HttpServletRequest req, HttpServletResponse rep, Exception e) {
        log.error("---DefaultException Handler---Host {}, invokes url {}, ERROR TYPE: {},  ERROR: {}", req.getRemoteHost(), req.getRequestURL(), e.getClass(), e.getMessage(), e);
        return RestResponse.fail(e.getMessage());
    }
}

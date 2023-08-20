package team.f5.api.support.advice

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import team.f5.api.support.exception.ApplicationException
import team.f5.api.support.exception.ErrorType
import team.f5.api.support.response.ApiResponse
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class ApiControllerAdvice {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(e: ApplicationException): ResponseEntity<ApiResponse<Any>> {
        when (e.errorType.logLevel) {
            LogLevel.ERROR -> log.error("ApplicationException : {}", e.message, e)
            LogLevel.WARN -> log.warn("ApplicationException : {}", e.message, e)
            else -> log.info("ApplicationException : {}", e.message, e)
        }

        return when {
            e.errorType.statusCode.is4xxClientError -> {
                val response = e.data?.let {
                    ApiResponse.fail(it)
                } ?: ApiResponse.fail(e.errorType)
                ResponseEntity.status(e.errorType.statusCode).body(response)
            }

            else -> ResponseEntity.status(e.errorType.statusCode).body(ApiResponse.error(e.errorType))
        }
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Any>> {
        log.error("Exception : {}", e.message, e)
        return ResponseEntity(ApiResponse.error(ErrorType.COMMON_ERROR), ErrorType.COMMON_ERROR.statusCode)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<ApiResponse<Any>> {
        log.error("Exception : {}", e.message, e)
        return ResponseEntity(e.message?.let { ApiResponse.error(it) }, HttpStatus.BAD_REQUEST)
    }
}

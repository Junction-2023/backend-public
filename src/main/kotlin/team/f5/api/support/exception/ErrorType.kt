package team.f5.api.support.exception

import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus

enum class ErrorType(val message: String, val statusCode: HttpStatus, val logLevel: LogLevel) {
    COMMON_ERROR("서버에 문제가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR),
    NOT_FOUND("엔티티를 찾을 수 없습니다", HttpStatus.NOT_FOUND, LogLevel.WARN),
    ILLEGAL_ARGUMENT("잘못된 인자가 전달되었습니다", HttpStatus.BAD_REQUEST, LogLevel.WARN),
    INVALID_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST, LogLevel.ERROR)
    ;
}

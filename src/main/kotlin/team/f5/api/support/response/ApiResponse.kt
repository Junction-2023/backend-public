package team.f5.api.support.response

import com.fasterxml.jackson.annotation.JsonInclude
import team.f5.api.support.exception.ErrorType

@JsonInclude(JsonInclude.Include.NON_NULL)
class ApiResponse<T> private constructor(
    val status: ApiStatus,
    val data: T? = null,
    val message: String? = null
) {
    companion object {
        fun <T> success(data: T? = null): ApiResponse<T> {
            return ApiResponse(ApiStatus.SUCCESS, data)
        }

        fun <T> fail(data: T? = null): ApiResponse<T> {
            return ApiResponse(ApiStatus.FAIL, data)
        }

        fun <T> fail(errorType: ErrorType): ApiResponse<T> {
            return ApiResponse(ApiStatus.ERROR, message = errorType.message)
        }

        fun <T> error(errorType: ErrorType): ApiResponse<T> {
            return ApiResponse(ApiStatus.ERROR, message = errorType.message)
        }

        fun <T> error(message: String): ApiResponse<T> {
            return ApiResponse(ApiStatus.ERROR, message = message)
        }
    }

    enum class ApiStatus {
        SUCCESS, FAIL, ERROR
    }
}

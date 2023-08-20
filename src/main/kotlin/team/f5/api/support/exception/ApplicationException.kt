package team.f5.api.support.exception

class ApplicationException(
    val errorType: ErrorType,
    val data: Any? = null
) : RuntimeException(errorType.message)

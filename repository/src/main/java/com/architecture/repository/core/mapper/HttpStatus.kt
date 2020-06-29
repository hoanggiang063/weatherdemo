package com.architecture.repository.core.mapper

import com.architecture.repository.core.mapper.HttpStatus.Series

enum class HttpStatus(private val value: Int, val reasonPhrase: String) {
    CONTINUE(100, "Continue"), SWITCHING_PROTOCOLS(101, "Switching Protocols"), PROCESSING(
        102,
        "Processing"
    ),
    CHECKPOINT(103, "Checkpoint"),  // 2xx Success
    OK(200, "OK"), CREATED(201, "Created"), ACCEPTED(
        202,
        "Accepted"
    ),
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"), NO_CONTENT(
        204,
        "No Content"
    ),
    RESET_CONTENT(205, "Reset Content"), PARTIAL_CONTENT(206, "Partial Content"), MULTI_STATUS(
        207,
        "Multi-Status"
    ),
    ALREADY_REPORTED(208, "Already Reported"), IM_USED(226, "IM Used"),  // 3xx Redirection
    MULTIPLE_CHOICES(300, "Multiple Choices"), MOVED_PERMANENTLY(301, "Moved Permanently"), FOUND(
        302,
        "Wso2 response"
    ),
    SEE_OTHER(303, "See Other"), NOT_MODIFIED(304, "Not Modified"), USE_PROXY(
        305,
        "Use Proxy"
    ),
    TEMPORARY_REDIRECT(307, "Temporary Redirect"), PERMANENT_REDIRECT(
        308,
        "Permanent Redirect"
    ),  // --- 4xx Client Error ---
    BAD_REQUEST(400, "Bad Request"), UNAUTHORIZED(401, "Unauthorized"), PAYMENT_REQUIRED(
        402,
        "Payment Required"
    ),
    FORBIDDEN(403, "Forbidden"), NOT_FOUND(404, "Not Found"), METHOD_NOT_ALLOWED(
        405,
        "Method Not Allowed"
    ),
    NOT_ACCEPTABLE(406, "Not Acceptable"), PROXY_AUTHENTICATION_REQUIRED(
        407,
        "Proxy Authentication Required"
    ),
    REQUEST_TIMEOUT(408, "Request Timeout"), CONFLICT(409, "Conflict"), GONE(
        410,
        "Gone"
    ),
    LENGTH_REQUIRED(411, "Length Required"), PRECONDITION_FAILED(
        412,
        "Precondition Failed"
    ),
    PAYLOAD_TOO_LARGE(413, "Payload Too Large"), URI_TOO_LONG(
        414,
        "URI Too Long"
    ),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"), REQUESTED_RANGE_NOT_SATISFIABLE(
        416,
        "Requested range not satisfiable"
    ),
    EXPECTATION_FAILED(417, "Expectation Failed"), I_AM_A_TEAPOT(
        418,
        "I'm a teapot"
    ),
    INSUFFICIENT_SPACE_ON_RESOURCE(419, "Insufficient Space On Resource"), METHOD_FAILURE(
        420,
        "Method Failure"
    ),
    DESTINATION_LOCKED(421, "Destination Locked"), UNPROCESSABLE_ENTITY(
        422,
        "Unprocessable Entity"
    ),
    LOCKED(423, "Locked"), FAILED_DEPENDENCY(424, "Failed Dependency"), UPGRADE_REQUIRED(
        426,
        "Upgrade Required"
    ),
    PRECONDITION_REQUIRED(428, "Precondition Required"), TOO_MANY_REQUESTS(
        429,
        "Too Many Requests"
    ),
    REQUEST_HEADER_FIELDS_TOO_LARGE(
        431,
        "Request Header Fields Too Large"
    ),
    UNAVAILABLE_FOR_LEGAL_REASONS(
        451,
        "Unavailable For Legal Reasons"
    ),  // --- 5xx Server Error ---
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"), NOT_IMPLEMENTED(
        501,
        "Not Implemented"
    ),
    BAD_GATEWAY(502, "Bad Gateway"), SERVICE_UNAVAILABLE(
        503,
        "Service Unavailable"
    ),
    GATEWAY_TIMEOUT(504, "Gateway Timeout"), HTTP_VERSION_NOT_SUPPORTED(
        505,
        "HTTP Version not supported"
    ),
    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"), INSUFFICIENT_STORAGE(
        507,
        "Insufficient Storage"
    ),
    LOOP_DETECTED(508, "Loop Detected"), BANDWIDTH_LIMIT_EXCEEDED(
        509,
        "Bandwidth Limit Exceeded"
    ),
    NOT_EXTENDED(510, "Not Extended"), NETWORK_AUTHENTICATION_REQUIRED(
        511,
        "Network Authentication Required"
    );

    /**
     * Return the reason phrase of this status code.
     */
    /**
     * Return the integer value of this status code.
     */
    fun value(): Int {
        return value
    }

    /**
     * Whether this status code is in the HTTP series
     * This is a shortcut for checking the value of [.series].
     */
    fun is1xxInformational(): Boolean {
        return Series.INFORMATIONAL == series()
    }

    /**
     * Whether this status code is in the HTTP series
     * This is a shortcut for checking the value of [.series].
     */
    fun is2xxSuccessful(): Boolean {
        return Series.SUCCESSFUL == series()
    }

    /**
     * Whether this status code is in the HTTP series
     * This is a shortcut for checking the value of [.series].
     */
    fun is3xxRedirection(): Boolean {
        return Series.REDIRECTION == series()
    }

    /**
     * Whether this status code is in the HTTP series
     * This is a shortcut for checking the value of [.series].
     */
    fun is4xxClientError(): Boolean {
        return Series.CLIENT_ERROR == series()
    }

    /**
     * Whether this status code is in the HTTP series
     * This is a shortcut for checking the value of [.series].
     */
    fun is5xxServerError(): Boolean {
        return Series.SERVER_ERROR == series()
    }

    /**
     * Returns the HTTP status series of this status code.
     *
     * @see Series
     */
    fun series(): Series {
        return Series.valueOf(this)
    }

    /**
     * Return a string representation of this status code.
     */
    override fun toString(): String {
        return Integer.toString(value)
    }

    /**
     * Enumeration of HTTP status series.
     *
     * Retrievable via [HttpStatus.series].
     */
    enum class Series(private val value: Int) {
        INFORMATIONAL(1), SUCCESSFUL(2), REDIRECTION(3), CLIENT_ERROR(4), SERVER_ERROR(5);

        /**
         * Return the integer value of this status series. Ranges from 1 to 5.
         */
        fun value(): Int {
            return value
        }

        companion object {
            fun valueOf(status: Int): Series {
                val seriesCode = status / 100
                for (series in values()) {
                    if (series.value == seriesCode) {
                        return series
                    }
                }
                throw IllegalArgumentException("No matching constant for [$status]")
            }

            fun valueOf(status: HttpStatus): Series {
                return valueOf(status.value)
            }
        }

    }

    companion object {
        /**
         * Return the enum constant of this type with the specified numeric value.
         *
         * @param statusCode the numeric value of the enum to be returned
         * @return the enum constant with the specified numeric value
         * @throws IllegalArgumentException if this enum has no constant for the specified numeric value
         */
        fun valueOf(statusCode: Int): HttpStatus {
            for (status in values()) {
                if (status.value == statusCode) {
                    return status
                }
            }
            throw IllegalArgumentException("No matching constant for [$statusCode]")
        }
    }

}
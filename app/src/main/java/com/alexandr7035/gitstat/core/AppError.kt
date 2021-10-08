package com.alexandr7035.gitstat.core

import java.io.IOException

data class AppError(val type: ErrorType): IOException()
package com.novacodestudios.exception

class AlreadyExistsException(message: String) : Exception(message)
class EntityNotFoundException(message: String? = null) : Exception(message)

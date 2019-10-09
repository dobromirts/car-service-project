package com.tsvetkov.autoservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND,reason = "Part not found!")
public class PartNotFoundException extends RuntimeException{

    public PartNotFoundException() {
    }

    public PartNotFoundException(String message) {
        super(message);
    }
}

package com.technicaltask.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactsRequest(
        @NotBlank String phone,
        @NotBlank @Email String email
) {
}

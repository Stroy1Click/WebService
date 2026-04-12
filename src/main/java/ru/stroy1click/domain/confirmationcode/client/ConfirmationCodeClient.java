package ru.stroy1click.domain.confirmationcode.client;

import ru.stroy1click.domain.confirmationcode.dto.CodeVerificationRequest;
import ru.stroy1click.domain.confirmationcode.dto.CreateConfirmationCodeRequest;
import ru.stroy1click.domain.confirmationcode.dto.UpdatePasswordRequest;

public interface ConfirmationCodeClient {

    void verifyEmail(CodeVerificationRequest request);

    void updatePassword(UpdatePasswordRequest request);

    void recreate(CreateConfirmationCodeRequest request);

    void create(CreateConfirmationCodeRequest request);
}

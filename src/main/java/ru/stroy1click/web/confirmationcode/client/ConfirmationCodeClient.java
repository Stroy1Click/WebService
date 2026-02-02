package ru.stroy1click.web.confirmationcode.client;

import ru.stroy1click.web.confirmationcode.dto.CodeVerificationRequest;
import ru.stroy1click.web.confirmationcode.dto.CreateConfirmationCodeRequest;
import ru.stroy1click.web.confirmationcode.dto.UpdatePasswordRequest;

public interface ConfirmationCodeClient {

    void verifyEmail(CodeVerificationRequest request);

    void updatePassword(UpdatePasswordRequest request);

    void recreate(CreateConfirmationCodeRequest request);

    void create(CreateConfirmationCodeRequest request);
}

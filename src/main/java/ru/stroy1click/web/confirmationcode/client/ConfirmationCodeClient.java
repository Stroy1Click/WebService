package ru.stroy1click.web.confirmationcode.client;

import ru.stroy1click.web.confirmationcode.model.CodeVerificationRequest;
import ru.stroy1click.web.confirmationcode.model.CreateConfirmationCodeRequest;
import ru.stroy1click.web.confirmationcode.model.UpdatePasswordRequest;

public interface ConfirmationCodeClient {

    void verifyEmail(CodeVerificationRequest request);

    void updatePassword(UpdatePasswordRequest request);

    void recreate(CreateConfirmationCodeRequest request);

    void create(CreateConfirmationCodeRequest request);
}

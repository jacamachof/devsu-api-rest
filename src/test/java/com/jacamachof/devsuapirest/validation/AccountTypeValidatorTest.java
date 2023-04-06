package com.jacamachof.devsuapirest.validation;

import com.jacamachof.devsuapirest.model.AccountType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountTypeValidatorTest {

    @Test
    void isValidTest_Null() {
        assertTrue(new AccountTypeValidator().isValid(null, null));
    }

    @Test
    void isValidTest_NotNull() {
        assertTrue(new AccountTypeValidator().isValid(AccountType.SAVING.getValue(), null));
    }

    @Test
    void isNotValidTest() {
        assertFalse(new AccountTypeValidator().isValid(UUID.randomUUID().toString(), null));
    }
}

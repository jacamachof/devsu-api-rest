package com.jacamachof.devsuapirest.utils;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SecurityUtilsTest {

    @Test
    void encodeTest() {
        var raw = UUID.randomUUID().toString();

        var encoded = SecurityUtils.encode(raw);

        assertNotNull(encoded);
    }

    @Test
    void matchesTrueTest() {
        var raw = UUID.randomUUID().toString();
        var encoded = SecurityUtils.encode(raw);

        assertTrue(SecurityUtils.matches(raw, encoded));
    }

    @Test
    void matchesFalseTest() {
        var raw = UUID.randomUUID().toString();
        var encoded = SecurityUtils.encode(raw);

        var raw2 = UUID.randomUUID().toString();

        assertFalse(SecurityUtils.matches(raw2, encoded));
    }
}

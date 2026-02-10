package com.involvest.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class HashUtilTest {
  @Test
  void sha256IsDeterministic() {
    String value = "involvest";
    assertEquals(HashUtil.sha256(value), HashUtil.sha256(value));
  }

  @Test
  void sha256DifferentInputs() {
    assertNotEquals(HashUtil.sha256("a"), HashUtil.sha256("b"));
  }
}

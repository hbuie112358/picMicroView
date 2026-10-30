package io.github.hbuie112358.picmicroview.core.alu;

/**
 * The outcome of an 8-bit addition: the byte written back and the STATUS flags it sets.
 *
 * @param result     the low 8 bits of the sum, 0 to 255
 * @param carry      C: a carry out of bit 7
 * @param digitCarry DC: a carry out of bit 3 (the low nibble)
 * @param zero       Z: the result is zero
 * @param overflow   OV: two's-complement overflow changed the sign bit
 * @param negative   N: bit 7 of the result is set
 */
public record AdditionResult(
        int result,
        boolean carry,
        boolean digitCarry,
        boolean zero,
        boolean overflow,
        boolean negative) {
}

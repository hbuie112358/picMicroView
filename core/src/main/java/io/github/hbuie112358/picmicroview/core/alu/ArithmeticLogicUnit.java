package io.github.hbuie112358.picmicroview.core.alu;

/**
 * The 8-bit arithmetic of the PIC18F452's ALU and the STATUS flags it produces.
 *
 * <p>Flag definitions are those of the STATUS register, datasheet DS39564C, Register 4-2
 * (printed page 52). Operands and results are unsigned byte values held in {@code int}s
 * (0 to 255), because Java's {@code byte} is signed and would sign-extend on every use.
 *
 * <p>Holds no state: every result depends only on the arguments (plan D14).
 */
public final class ArithmeticLogicUnit {

    private static final int BYTE_MASK = 0xFF;
    private static final int LOW_NIBBLE_MASK = 0x0F;
    private static final int SIGN_BIT = 0x80;

    private ArithmeticLogicUnit() {
    }

    /**
     * Adds two bytes as ADDWF and ADDLW do (datasheet page 217): {@code augend + addend},
     * kept to 8 bits, with C, DC, Z, OV and N.
     *
     * @throws ByteOutOfRangeException if either operand is outside 0 to 255
     */
    public static AdditionResult add(int augend, int addend) {
        requireByte("augend", augend);
        requireByte("addend", addend);

        int fullSum = augend + addend;
        int result = fullSum & BYTE_MASK;
        return new AdditionResult(
                result,
                fullSum > BYTE_MASK,
                (augend & LOW_NIBBLE_MASK) + (addend & LOW_NIBBLE_MASK) > LOW_NIBBLE_MASK,
                result == 0,
                hasSignedOverflow(augend, addend, result),
                (result & SIGN_BIT) != 0);
    }

    /*
     * Two's-complement overflow happens exactly when both operands have the same sign and the
     * result's sign differs from it (adding two positives gives a negative, or two negatives a
     * positive). (augend ^ result) has bit 7 set when the result's sign differs from the
     * augend's, (addend ^ result) likewise for the addend; both at once means the operands
     * agreed with each other and the result disagrees with both.
     */
    private static boolean hasSignedOverflow(int augend, int addend, int result) {
        return ((augend ^ result) & (addend ^ result) & SIGN_BIT) != 0;
    }

    private static void requireByte(String operandName, int value) {
        if (value < 0 || value > BYTE_MASK) {
            throw new ByteOutOfRangeException(operandName, value);
        }
    }
}

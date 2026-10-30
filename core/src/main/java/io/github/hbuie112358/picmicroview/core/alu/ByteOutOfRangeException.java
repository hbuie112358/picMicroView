package io.github.hbuie112358.picmicroview.core.alu;

import java.io.Serial;

/**
 * Raised when a value meant to be one byte of PIC data lies outside 0 to 255. Such a value can
 * only come from a bug in the simulator itself, never from a user's program, since registers
 * hold 8 bits.
 */
public final class ByteOutOfRangeException extends IllegalArgumentException {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String UNSPECIFIED_OPERAND = "[UNSPECIFIED_OPERAND]";

    private final String operandName;
    private final int value;

    public ByteOutOfRangeException(String operandName, int value) {
        // Built defensively: constructing this exception must never fail itself. Java 25's
        // flexible constructor bodies allow computing the safe name once, before super().
        String safeOperandName = operandName != null ? operandName : UNSPECIFIED_OPERAND;
        super("%s must be a byte value (0 to 255) but was %d".formatted(safeOperandName, value));
        this.operandName = safeOperandName;
        this.value = value;
    }

    public String operandName() {
        return operandName;
    }

    public int value() {
        return value;
    }
}

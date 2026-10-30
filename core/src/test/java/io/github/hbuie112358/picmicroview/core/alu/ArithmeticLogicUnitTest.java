package io.github.hbuie112358.picmicroview.core.alu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ArithmeticLogicUnitTest {

    @Nested
    class Add {

        /*
         * Expected values worked out by hand from the STATUS flag definitions (datasheet
         * Register 4-2, page 52), one row per case where something different happens. The first
         * two rows are the datasheet's own ADDLW and ADDWF examples (page 217).
         */
        @ParameterizedTest(name = "{0} + {1} = {2}: C={3} DC={4} Z={5} OV={6} N={7} ({8})")
        @CsvSource(textBlock = """
                0x10, 0x15, 0x25, false, false, false, false, false, datasheet ADDLW example
                0x17, 0xC2, 0xD9, false, false, false, false, true,  datasheet ADDWF example
                0x01, 0x01, 0x02, false, false, false, false, false, plain sum sets no flag
                0x00, 0x00, 0x00, false, false, true,  false, false, zero result alone
                0x08, 0x07, 0x0F, false, false, false, false, false, largest low nibble without digit carry
                0x08, 0x08, 0x10, false, true,  false, false, false, low nibble carries out
                0x80, 0x7F, 0xFF, false, false, false, false, true,  largest sum without carry
                0x0F, 0x01, 0x10, false, true,  false, false, false, low nibble wraps
                0xFF, 0x01, 0x00, true,  true,  true,  false, false, wraps to zero
                0x7F, 0x01, 0x80, false, true,  false, true,  true,  two positives give a negative
                0x80, 0x80, 0x00, true,  false, true,  true,  false, two negatives give zero
                0x80, 0xFF, 0x7F, true,  false, false, true,  false, two negatives give a positive
                0xFF, 0xFF, 0xFE, true,  true,  false, false, true,  largest operands
                """)
        void setsResultAndFlagsAsTheDatasheetDefines(
                String augend, String addend, String result,
                boolean carry, boolean digitCarry, boolean zero, boolean overflow, boolean negative,
                String reason) {
            AdditionResult sum = ArithmeticLogicUnit.add(Integer.decode(augend), Integer.decode(addend));

            assertThat(sum).as(reason).isEqualTo(new AdditionResult(
                    Integer.decode(result), carry, digitCarry, zero, overflow, negative));
        }

        @ParameterizedTest(name = "{0} + {1} rejects {2} = {3}")
        @CsvSource({
                "-1,  0,   augend, -1",
                "256, 0,   augend, 256",
                "0,   -1,  addend, -1",
                "0,   256, addend, 256"})
        void rejectsOperandOutsideAByte(int augend, int addend, String rejectedOperand, int rejectedValue) {
            assertThatThrownBy(() -> ArithmeticLogicUnit.add(augend, addend))
                    .isInstanceOfSatisfying(ByteOutOfRangeException.class, outOfRange -> {
                        assertThat(outOfRange.operandName()).isEqualTo(rejectedOperand);
                        assertThat(outOfRange.value()).isEqualTo(rejectedValue);
                    });
        }

        @Test
        void acceptsBothEndsOfTheByteRange() {
            assertThat(ArithmeticLogicUnit.add(0x00, 0xFF).result()).isEqualTo(0xFF);
        }
    }
}

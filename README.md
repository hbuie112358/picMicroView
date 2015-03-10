# picMicroView
picMicroView is an emulator for a PIC18F452 microprocessor. Its main purpose is to educate by allowing the user to step through instructions one at a time and inspect the corresponding discrete internal state of the microprocessor. See the screen shot below.

**New in version 0.7: 1. Integrated view of program memory, data memory, and .lst file, with program memory and specific line in .lst file highlighted with current instruction when stepping. 2. Separate Ports/Registers table showing Special Function Registers. 3. Ports/Registers table and Data Memory table highlight which addresses were written to by previous instruction when in step mode. 4. Commented examples of many instructions along with general concepts such as indirect addressing and assembly language loops. 5. Ports A through E update Ports/Registers table in real time as program runs. 6. Indirect addressing with INDF, PLUSW, POSTDEC, POSTINC, PREINC. 7. Banked addressing with BSR. More instructions and features on the way with next release around April 1.

To try picMicroView, download the .jar file under the "Releases" tab. Make sure you have at least Java SE 8 installed. Load the examples under the "Examples" menu. Most of the examples are meant for stepping. The example under "General Concepts," "Loops," "Fast PORTA Toggle" runs in a loop, and toggles a bit in Port A quickly.

To run your own assembly language program, you need an assembler. One suggestion is the open source gpasm program. Once your program is assembled, you can load the .hex file produced by the assembler into picMicroView via the "Load" menu. Future plans include embedding an editor and a link to external assembler into this program.

Thank you for looking, and please keep in touch!

Currently implemented PIC18 instructions:

addlw, addwf, addwfc, andlw, bc, bcf, bn, bnc, bnn, bnov, bnz, bov, bra, bsf, btfsc, btfss, btg, bz, clrf, comf, cpfseq, cpfsgt, cpfslt, dcfsnz, decf, decfsz, goto, incf, incfsz, infsnz, iorwf, lfsr, movf, movff, movlb, movlw, movwf, mullw, mulwf, nrgf, nop, rcall, return, rlcf, rlfnc, sublw

<img src=http://i.imgur.com/thVfuFN.jpg>


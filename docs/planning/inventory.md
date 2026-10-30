# Inventory: datasheet, Swing version, examples

*Written 2026-10-30 for plan step 3. In this file "the user" is the person Claude works with, and
"Claude" is the assistant.*

What the PIC18F452 can do (datasheet DS39564C, `reference/39564c.pdf`, see plan step 3), what the
Swing version implements, and which `examples/` programs exercise each part. It records what
exists, not whether it is correct: behaviour is checked against the datasheet in steps 6–9.

**Uses:** the checklist for deleting `legacy/` (D19), the instruction list for steps 7 and 8, the
scenario tests per instruction (step 8), and the place where known findings attach.

**How it was built:** the instruction list from datasheet Table 20-2 (printed pages 214–216); page
numbers from each instruction's own description in section 20.1, which is the authority because
Table 20-2 has printing errors (plan finding F7). Swing classes from `src/picmicroview/model/`;
"implemented" means the decoder in `Pic18F452.java` creates the class, which is true for all 59.
Examples from the folder names under `examples/`.

## Summary

- The datasheet has **69 instructions** (75 counting the eight forms of `TBLRD` and `TBLWT`).
- The Swing version implements **59**. All are wired into the decoder; each has a `basic/`
  example, and 48 have an `inDepth/` example.
- **10 are missing:** `CALL`, `CLRWDT`, `POP`, `PUSH`, `RESET`, `RETFIE`, `RETLW`, `SLEEP`,
  `TBLRD`, `TBLWT`. None has an example, so in step 8 they are checked against the datasheet
  alone.

## Instructions

Page: the printed page of the instruction's description in section 20.1 (PDF page = printed
page + 2). Examples: `basic/` and `inDepth/` mean `examples/<group>/<first letter>/<instruction>/`
exists, each with `.asm`, `.hex`, `.lst` and `.cod` from gpasm 0.13.7.

### Byte-oriented file register operations (31)

| Instruction | Page | Swing class | Examples | Notes |
|---|---|---|---|---|
| ADDWF | 217 | `Addwf` | basic, inDepth | F3 (DC, OV not updated), F4 (FSR carry) |
| ADDWFC | 218 | `Addwfc` | basic, inDepth | Table 20-2 misprints its encoding (F7) |
| ANDWF | 219 | `Andwf` | basic, inDepth | |
| CLRF | 227 | `Clrf` | basic, inDepth | |
| COMF | 228 | `Comf` | basic, inDepth | |
| CPFSEQ | 228 | `Cpfseq` | basic, inDepth | |
| CPFSGT | 229 | `Cpfsgt` | basic, inDepth | |
| CPFSLT | 229 | `Cpfslt` | basic, inDepth | |
| DECF | 230 | `Decf` | basic, inDepth | |
| DECFSZ | 231 | `Decfsz` | basic, inDepth | |
| DCFSNZ | 231 | `Dcfsnz` | basic, inDepth | |
| INCF | 232 | `Incf` | basic, inDepth | |
| INCFSZ | 233 | `Incfsz` | basic, inDepth | |
| INFSNZ | 233 | `Infsnz` | basic, inDepth | |
| IORWF | 234 | `Iorwf` | basic, inDepth | |
| MOVF | 235 | `Movf` | basic, inDepth | |
| MOVFF | 236 | `Movff` | basic | two-word instruction |
| MOVWF | 237 | `Movwf` | basic, inDepth | |
| MULWF | 238 | `Mulwf` | basic | result in PRODH:PRODL |
| NEGF | 239 | `Negf` | basic, inDepth | |
| RLCF | 243 | `Rlcf` | basic, inDepth | |
| RLNCF | 244 | `Rlncf` | basic, inDepth | also the misnamed `rlfnc` examples (F8) |
| RRCF | 244 | `Rrcf` | basic, inDepth | |
| RRNCF | 245 | `Rrncf` | basic, inDepth | |
| SETF | 245 | `Setf` | basic, inDepth | |
| SUBFWB | 246 | `Subfwb` | basic, inDepth | |
| SUBWF | 247 | `Subwf` | basic, inDepth | |
| SUBWFB | 248 | `Subwfb` | basic, inDepth | |
| SWAPF | 248 | `Swapf` | basic, inDepth | |
| TSTFSZ | 251 | `Tstfsz` | basic, inDepth | |
| XORWF | 252 | `Xorwf` | basic, inDepth | |

### Bit-oriented file register operations (5)

| Instruction | Page | Swing class | Examples | Notes |
|---|---|---|---|---|
| BCF | 220 | `Bcf` | basic, inDepth | |
| BSF | 223 | `Bsf` | basic, inDepth | |
| BTFSC | 224 | `Btfsc` | basic, inDepth | |
| BTFSS | 224 | `Btfss` | basic, inDepth | |
| BTG | 225 | `Btg` | basic, inDepth | Table 20-2 misprints its operands as `f, d, a` (F7) |

### Control operations (22)

| Instruction | Page | Swing class | Examples | Notes |
|---|---|---|---|---|
| BC | 219 | `Bc` | basic, inDepth | |
| BN | 220 | `Bn` | basic, inDepth | |
| BNC | 221 | `Bnc` | basic, inDepth | |
| BNN | 221 | `Bnn` | basic, inDepth | |
| BNOV | 222 | `Bnov` | basic, inDepth | |
| BNZ | 222 | `Bnz` | basic | Table 20-2 gives 2 cycles, the other branches 1 (2); check on page 222 |
| BOV | 225 | `Bov` | basic, inDepth | out of alphabetical order in the datasheet |
| BRA | 223 | `Bra` | basic | |
| BZ | 226 | `Bz` | basic, inDepth | |
| CALL | 226 | — | — | missing; two-word absolute call (`RCALL` exists) |
| CLRWDT | 227 | — | — | missing; no watchdog timer modelled |
| DAW | 230 | `Daw` | basic, inDepth | |
| GOTO | 231 | `Goto` | basic | two-word instruction |
| NOP | 239 | `Nop` | basic | two encodings (`0x0000` and `0xFxxx`, the second word of two-word instructions) |
| POP | 240 | — | — | missing |
| PUSH | 240 | — | — | missing |
| RCALL | 241 | `Rcall` | basic | |
| RESET | 241 | — | — | missing |
| RETFIE | 242 | — | — | missing; no interrupt support at all |
| RETLW | 242 | — | — | missing; the usual way to build lookup tables |
| RETURN | 243 | `Return` | basic | |
| SLEEP | 246 | — | — | missing |

### Literal operations (9, plus RETLW listed under control)

| Instruction | Page | Swing class | Examples | Notes |
|---|---|---|---|---|
| ADDLW | 217 | `Addlw` | basic, inDepth | |
| ANDLW | 218 | `Andlw` | basic | |
| IORLW | 234 | `Iorlw` | basic, inDepth | |
| LFSR | 235 | `Lfsr` | basic | two-word instruction; decoded from the second word |
| MOVLB | 236 | `Movlb` | basic | |
| MOVLW | 237 | `Movlw` | basic | |
| MULLW | 238 | `Mullw` | basic, inDepth | |
| SUBLW | 247 | `Sublw` | basic, inDepth | |
| XORLW | 251 | `Xorlw` | basic, inDepth | |

### Data memory ↔ program memory operations (2 mnemonics, 8 forms)

| Instruction | Page | Swing class | Examples | Notes |
|---|---|---|---|---|
| TBLRD (`*`, `*+`, `*-`, `+*`) | 249 | — | — | missing; reads program memory (constant tables) |
| TBLWT (`*`, `*+`, `*-`, `+*`) | 250 | — | — | missing; writes program memory |

## Besides instructions

From the class names and a first look; how complete each part is gets checked in the step that
rebuilds it.

| Part | In the Swing version | Examples | Rebuilt in |
|---|---|---|---|
| Program loading (Intel HEX) | `Parser`, `Scanner`, `Record`, `RecType` | all `.hex` files | step 5 |
| Data memory, banking | `DataMemory`, `BSR` (with `MOVLB`) | — | step 6 |
| Indirect addressing | `FsrL`, `FsrH`, `Indf`, `Postinc`, `Postdec`, `Preinc`, `Plusw` | `indirectAddressing/` (indf, plusw, postdec, postinc, preinc) | step 6 |
| Core registers | `Wreg`, `Status`, `PCL`, `PclatH`, `PclatU`, `ProgramCounter`, `Stack`, `Stkptr`, `ProdL`, `Prodh` | — | steps 6–9 |
| I/O ports | `PortA` to `PortE` | `loops/` (fastPortAToggle, slowToggleAllPorts) | steps 6, 10 (D5, D6) |
| Timer0 | `Timer`, `Clock`, `CycleTime` | — | step 9 (F5) |
| Interrupts | `Intcon` is a plain register; nothing handles interrupts | — | later |
| User interface | Swing `MainWindow`: Load, New, Quit; Run, Step, Stop; an examples menu loading each example by name; per-register views for run and step | — | step 10 |
| Assembler | an **Assemble** menu item with no action attached (a placeholder) | — | step 11 |
| Tests | `TestMain`, `TestInstruction` | — | replaced (F1, F2) |

**Not in the Swing version** (datasheet chapters with no counterpart): interrupts (ch. 8), Timer1–3
(ch. 11–13), CCP/PWM (ch. 14), MSSP (ch. 15), USART (ch. 16), A/D (ch. 17), data EEPROM (ch. 6),
the watchdog timer and sleep (ch. 19). The plan lists other timers and peripherals as later work.

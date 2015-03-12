;;;;;;; tstfsz.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of tstfsz according to the examples in the 
;pic18 user manual. Tstfsz tests the contents of register f and skips the 
;next instruction if contents = 0. 
;
;;;;;;; Program hierarchy ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Mainline
;  
;
;;;;;;; Assembler directives ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

        list  P=PIC18F452, F=INHX32, C=160, N=0, ST=OFF, MM=OFF, R=DEC, X=ON
        #include p18f452.inc

;;;;;;; Variables ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

	cblock  0x000           ;Beginning of Access RAM
	REG1
	endc

	cblock	0x09a
	REG2
	endc

;;;;;;; Macro definitions ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;; Vectors ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


        org  0x0000             ;Reset vector
        nop
        goto  Mainline

        org  0x0008             ;High priority interrupt vector
        goto  $                 ;Trap

        org  0x0018             ;Low priority interrupt vector
        goto  $                 ;Trap

;;;;;;; Mainline program ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

Mainline

;Example 1A, 2:

		movlb	0x01		;Set BSR to 0x01 for banked addressing
		movlw	0xaf		;Place 0xaf in wreg
		movwf	REG1, 1		;Copy wreg value into REG1
HERE		tstfsz	REG1, 1		;Test register f, if zero skip next instruction
NZERO		goto	PROCESS_CODE
ZERO		goto 	MORE_CODE


PROCESS_CODE	nop			;Came to PROCESS_CODE since value in f is not 0
		movlw	0x00		;Place 0x00 in wreg
		movwf	REG1, 1		;Copy wreg value into REG1
		goto	HERE

;Example 3:
;Cases 1, 2:

MORE_CODE	movlb	0x0f		;Select bank 15 of data memory
		movlw	0x00		;Place 0x00 in wreg
		movwf	REG2, 1		;Copy contents of wreg into 0xf9a and 0x09a
		movlb	0x03		;Select bank 3 of data memory
		movlw	0xaf		;Place 0xaf in wreg
		movwf	REG2, 1		;Copy contents of wreg into 0x39a

HERE_3		tstfsz	REG2, 0		;Test register f, if zero skip next instruction
NZERO_3		goto	EXAMPLE_4
ZERO_3		goto 	AGAIN

AGAIN		movlw	0x00		;Place 0x00 in wreg
		movwf	REG2, 1		;Copy contents of wreg to 0x39a
		movlb	0x0f		;Select bank 15 of data memory
		movlw	0xaf		;Place 0xaf in wreg
		movwf	REG2, 1		;Copy wreg to 0xf9a
		goto 	HERE_3

;Example 4:
;Cases 1, 2:

EXAMPLE_4	lfsr	0, 0x6c2	;Place 0x6c2 in FSR0
		movlw	0xaf		;Place 0xaf in wreg
		movwf	INDF0		;Copy wreg to register pointed to by FSR0
HERE_4		tstfsz	INDF0, 1	;If contents of register pointed to by FSR0 is 
					;zero, skip next program memory address
NZERO_4		goto	CONTINUE
ZERO_4		goto 	stop
CONTINUE	movlw	0x00		;Place 0x00 in wreg
		movwf	INDF0		;Copy wreg to register pointed to by FSR0
		goto	HERE_4


stop	goto	stop

	end



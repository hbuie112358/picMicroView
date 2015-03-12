;;;;;;;andwf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of the andwf instruction using the examples starting
;on page 680 of the PIC 18 user manual. Andwf performs an AND operation of the contents
;of wreg and contents of register f.
;
;
;;;;;;; Program hierarchy ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Mainline
;  
;
;;;;;;; Assembler directives ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

	#include p18f452.inc
        list  P=PIC18F452, F=INHX32, C=160, N=0, ST=OFF, MM=OFF, R=DEC, X=ON

;;;;;;; Variables ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

	cblock  0x000           ;Beginning of Access RAM
	REG1
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


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;See manual chapter 31 for more detail on individual instructions
;Syntax: 	andwf	f, d, a		(register, destination register, bank select)
					;"d", "a" both default to 0. For d, can use "f" 
					;instead of 1, can use "w" instead of 0.

;		movwf	f, a		(register, bank select)
;		movlw	k		(literal)
;		movlb	k		(literal)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
Mainline

;Example 1:

	movlb	1		;Set BSR to bank 1
	movlw	0xc2		;Place 0xc2 in wreg
	movwf	REG1, 1		;Copy wreg to REG1
	movlw 	0x17 		;Place 0x17 in wreg
	andwf	REG1, 1, 1	;And contents of REG1 with wreg, result in REG1

;Example 2:
	
	movlw	0xc2		;Place 0xc2 in wreg
	movwf	REG1, 1		;Copy wreg to REG1
	movlw 	0x17 		;Place 0x17 in wreg
	andwf	REG1, 0, 1	;And contents of REG1 with wreg, result in wreg

;Example 3:
;Case 1:

	lfsr	0, 0xfc2	;Place 0xfc2 in FSR0
	movlw	0x5a		;Place 0x5a in wreg
	movwf	INDF0		;Copy wreg value into location pointed to by FSR0
	movlw	0x17		;Place 0x17 in wreg
	andwf	INDF0, 1, 1	;Using banked addressing, and contents of register 
				;pointed to by FSR0 with wreg, result in INDF0
	
;Case 2:

	lfsr	0, 0x4c2	;Place 0x4c2 in FSR0
	movlw	0x5a		;Place 0x5a in wreg
	movwf	INDF0		;Copy wreg value into location pointed to by FSR0
	movlw	0x00		;Place 0x00 in wreg
	andwf	INDF0, 1, 1	;Using banked addressing, and contents of register 
				;pointed to by FSR0 with wreg, result in INDF0

stop	goto		stop

	end

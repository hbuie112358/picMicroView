;;;;;;;addwfc.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of the addwfc instruction using the examples starting
;on page 676 of the PIC 18 user manual. It adds the contents of the WREG and the 
;carry bit to the contents of register f.
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
	temp
	endc

;Define variable temp1 at ram address 0x100
	cblock  0x100           ;Beginning of bank 1 RAM
	tempBank1
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
;Syntax: 	addwfc	f, d, a		(register, destination register, bank select)
					;"d", "a" both default to 0. For d, can use "f" 
					;instead of 1, can use "w" instead of 0.

;		movwf	f, a		(register, bank select)
;		movlw	k		(literal)
;		movlb	k		(literal)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
Mainline

;Example 1, Case 1:

	movlb	1		;Set BSR to bank 1
	lfsr	0, 0x9c2	;Place 0x9c2 in FSR0
	movlw	0x17		;Place 0x17 in wreg
	addwfc	FSR0L, 0, 1	;Using banked addressing add with carry wreg, FSR0L,
				;place value in wreg (ignores "a" bit of 1 because 
				;register argument is FSR, part of INDF scheme

;Example 1, Case 2:
	
	lfsr	0, 0x7c2	;Place 0x7c2 in FSR0
	movlw	0x17		;Place 0x17 in wreg
	bsf	STATUS, 0	;Set carry bit in status register
	addwfc	FSR0L, 0, 1	;Using banked addressing, add with carry wreg, FSR0L,
				;place value in wreg (ignores "a" bit of 1 because 
				;register argument is FSR, part of INDF scheme

;Example 2:

	lfsr	0, 0x0c2	;Place 0x0c2 in FSR
	movlw	0x20		;Place 0x20 in wreg
	movwf	INDF0		;Copy wreg value into location pointed to by INDF0
	movlw	0x17		;Place 0x17 in wreg
	addwfc	INDF0, 1, 1	;Using banked addressing, add with carry wreg, INDF0,
				;place value in INDF0 (ignores "a" bit of 1 because 
				;register argument is an INDF register
	

;General tests:

;Test no carry, use bsr, write wreg:

	movlw	0x17
	movlb	1
	movwf	tempBank1	;Store 0x17 at address 0x100
	movlw	0xc2
	addwfc	tempBank1, 0, 1	;Add WREG, C bit, and temp, place result (0xd9) in WREG (no carry)

;Test no carry, use bsr, write freg:
	movlw	0x18
	movlb	1
	movwf	tempBank1	;Store 0x18 at address 0x100
	movlw	0xc2
	addwfc	tempBank1, 1, 1	;Add WREG, C bit, and temp, place result (0xd9) in tempBank1 (no carry)

;Test no carry, no bsr, write wreg:

	movlw	0x17
	movlb	1
	movwf	temp		;Store 0x17 at address 0x100
	movlw	0xc2
	addwfc	temp, 0		;Add WREG, C bit, and temp, 
				;place result (0xd9) in WREG (no carry)

;Test no carry, no bsr, write freg:
	movlw	0x18
	movlb	1
	movwf	temp		;Store 0x18 at address 0x000
	movlw	0xc2
	addwfc	temp, 1		;Add WREG, C bit, and temp, 
				;place result (0xd9) in temp (no carry)

;Cause carry bit to be set:

	movlw	0xfe
	movwf	temp
	movlw	0xad
	addwfc	temp, 0		;Set carry bit

;Test add with carry:

	movwf	temp		
	movlw	0x02
	addwfc	temp, 0		;Add WREG, C bit, and temp (0x02 + 1 + 0xab), 
				;place result (0xae) in WREG (clears carry bit)

stop	goto		stop

	end






















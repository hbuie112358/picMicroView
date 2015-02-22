;;;;;;; iorwf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of iorwf, inclusive OR the WREG with 
;contents of register f using the examples starting on page 736 of 
;the PIC18 user manual.
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

	cblock	0x100		;Beginning of 0x100 block, just above Access Ram
	RESULT
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


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;See manual chapter 31 for more detail on individual instructions
;Syntax: 	iorwf	f, d, a		(register, destination register, bank select)
					;"d", "a" both default to 0. For d, can use "f" 
					;instead of 1, can use "w" instead of 0.

;		movwf	f, a		(register, bank select)
;		movlw	k		(literal)
;		movlb	k		(literal)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;; Mainline program ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

Mainline

;Example 1:

	movlb	1
	lfsr	0, 0xdc2
	movlw	0x30
	movwf	INDF0		;Copy 0x30 to INDF0:		0011 0000
	movlw	0x17		;Place 0x17 in wreg:		0001 0111
	iorwf	INDF0, 1, 1	;Bit wise OR wreg, INDF0:	0011 0111
				;Ignores "a" bit of 1 

;Example 2, Case 1:
	
	movlw	0x13		;Place 0x13 in wreg:		0001 0011
	movwf	RESULT, 1	;Copy to RESULT
	movlw	0x91		;Place 0x91 in wreg:		1001 0001
	iorwf	RESULT, 1, 1	;Bit wise OR wreg, INDF0:	1001 0011
	
;Example 2, Case 2:

	movlw	0x00		;Place 0x00 in wreg:		0000 0000
	movwf	RESULT, 1	;Copy to RESULT			0000 0000
	iorwf	RESULT, 1, 1	;Bit wise OR wreg, INDF0:	0000 0000
				;Z bit is set
;General tests:
	movlw	0x35		;Place 0x35 in wreg
	movwf	temp		;Copy value in wreg to temp1
	movlw	0xca		;Place 0xca in wreg		
	iorwf	temp, 0		;Inclusive OR wreg with temp1, result in wreg
	movlw	0x2b		;Place 0x2b in wreg
	movwf	temp		;Copy value in wreg to temp1
	movlw	0xd4		;Place 0xd4 in wreg
	iorwf	temp, 1		;Inclusive OR wreg with temp1, result in temp

;Test banked addressing:
		
	movlb	0x01		;Place 0x01 in bank select register
	movlw	0x35		;Place 0x35 in wreg
	movwf	tempBank1, 1	;Using bank select, copy value in wreg to temp2
	movlw	0xca		;Place 0xca in wreg
	iorwf	tempBank1, f, 1	;Using bank select, inclusive OR wreg with temp2, result in temp2
	iorwf	tempBank1, w, 1	;Using bank select, inclusive OR wreg with temp2, result in wreg
		

stop		goto		stop

		end

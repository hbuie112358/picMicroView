;;;;;;; movf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of movf instruction according to the examples
;on page 740 of the PIC18 user manual. Contents of register f
;are moved to either f or wreg
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
	MYREG
	endc

	cblock	0x100		;Beginning of 0x100 address block, just above Access Ram
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
;Syntax: 	movf	f, d, a		(register, destination register, bank select)
					;"d", "a" both default to 0. For d, can use "f" 
					;instead of 1, can use "w" instead of 0.

;		movwf	f, a		(register, bank select)
;		movlw	k		(literal)
;		movlb	k		(literal)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;; Mainline program ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

Mainline

;Example 1:
	

	movlb	1		;Place 1 in BSR
	movlw	0x22		;Place 0x22 in wreg
	movwf	MYREG, 1	;Copy wreg value to MYREG
	movlw	0x00		;Place 0x00 in wreg
	decf	WREG, w		;Decrement wreg, set status bit "N" to 1
	movf	MYREG, 0, 1	;Using bank select, move value in MYREG to wreg, 
				;Set status bit "N" to 0

;Example 2:

	movlw	0x00		;Place 0 in wreg
	movwf	MYREG, 1	;Using banked addressing, copy wreg value to MYREG
	movlw	0x10		;Place 0x10 in wreg
	movf	MYREG, 1, 1	;Using banked addressing, move MYREG value to itself,
				;Set status bit "Z" to 1

;Example 3, Case 1:
	
	movlw	0x00		;Place 0 in wreg
	movwf	MYREG, 0	;Copy wreg to MYREG in access bank
	movlw	0x10		;Place 10 in wreg
	movf	MYREG, 1, 0	;Using banked addressing (0 means use access bank), 
				;move MYREG value to itself, set status bit "Z"

;Example 3, Case 2:

	movlw	0x80		;Place 0x80 in wreg
	movwf	MYREG, 0	;Copy wreg to MYREG in access bank
	movlw	0x10		;Place 0x10 in wreg
	movf	MYREG, 1, 0	;Using banked addressing (0 means use access bank), 
				;move MYREG value to itself, set status bit "N"

;General testing:

	movlw	0x00		;Place 0 in wreg
	movwf	temp		;Set value in temp1 equal to value in wreg
	movf	temp, f		;Place the value in temp1 into itself (adjusts Z, N flags)
	movlw	0x80		;Place 0x80 in wreg
	movwf	temp		;Set value in temp1 equal to value in wreg
	movlw	0x00		;Place 0 in wreg
	movf	temp, w		;Place the value in temp1 into wreg (adjusts Z, N flags)

;Test with banked addressing:

	movlb	0x01		;Set value in bank select register (BSR) to 1 to indicate bank 1
	movlw	0xaa		;Place 0xaa in wreg
	movwf	tempBank1	;Using BSR, set value in tempBank1  equal to value in wreg
	movf	tempBank1, f	;Using BSR, place the value in tempBank1 into itself (adjusts Z, N flags)

stop	goto	stop

	end























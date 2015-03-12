;;;;;;;daw.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of the daw instruction. Daw adjusts the eight 
;bit value in wreg to produce a packed bcd result.
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
;Syntax: 	daw

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
Mainline

;Example 1:
;Case 1:

	movlw	0x09		;Place 0x09 in wreg (as packed BCD number 09)
	movwf	REG1		;Copy wreg to REG1
	movlw	0x05		;Place 0x05 in wreg (as packed BCD number 05)
	addwf	REG1, 0		;Add wreg and REG1, result in wreg (result is 0x0d)
	daw			;Decimal adjust wreg, result is packed BCD number 14

;Case 2:
	
	movlw	0x48		;Place 0x48 in wreg (as packed BCD number 48)
	movwf	REG1		;Copy wreg to REG1
	movlw	0x47		;Place 0x47 in wreg (as packed BCD number 47)
	addwf	REG1, 0		;Add wreg and REG1, result in wreg (result is 0x8f)
	daw			;Decimal adjust wreg, result is packed BCD number 95

;Case 3:

	movlw	0x42		;Place 0x42 in wreg (as packed BCD number 42)
	movwf	REG1		;Copy wreg to REG1
	movlw	0x97		;Place 0x97 in wreg (as packed BCD number 97)
	addwf	REG1, 0		;Add wreg and REG1, result in wreg (result is 0xd9)
	daw			;Decimal adjust wreg, result is 139, with packed BCD
				;number 39 in wreg and the 1 represented in status 
				;register carry bit.

;Case 4:

	movlw	0x97		;Place 0x97 in wreg (as packed BCD number 97)
	movwf	REG1		;Copy wreg to REG1
	addwf	REG1, 0		;Add wreg and REG1, result in wreg (result is 0x2e),
				;with status register carry bit set
	daw			;Decimal adjust wreg, result is 194, with packed BCD
				;number 94 in wreg and the 1 represented in status 
				;register carry bit.


stop	goto		stop

	end

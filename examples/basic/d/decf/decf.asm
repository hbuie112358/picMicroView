;;;;;;; decfExample.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
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
;Syntax: 	decf	f, d, a		(register, destination register, bank select)
					;"d", "a" both default to 0. For d, can use "f" 
					;instead of 1, can use "w" instead of 0.

;		movwf	f, a		(register, bank select)
;		movlw	k		(literal)
;		movlb	k		(literal)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;; Mainline program ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

Mainline

	movlw	5		;Place 5 in wreg
	movwf	temp		;Place value of wreg in temp
	decf	temp, w		;Decrement value in temp, place result in wreg
	decf	temp, 0		;Same thing, wreg stays same since temp never changed
	decf 	temp, f		;Decrement value in temp, place result in temp
	decf 	temp, f		;Decrement value in temp, place result in temp
	decf 	temp, f		;Decrement value in temp, place result in temp
	decf 	temp, f		;Decrement value in temp, place result in temp
	decf	temp, 1		;Decrement, status register bit 2 (Z) goes high
	decf	temp, 1		;Decrement, status register bit 4 (N) goes high, (Z) low
	movlw	B'10000000'	;Place 128 in wreg
	decf	WREG, w		;Decrement, status register bit 4 (N) goes low
					
stop	goto	stop

	end






















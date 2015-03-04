;;;;;;; decf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
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
	CNT
	endc

;	cblock	0x100		;Beginning of 0x100 block, just above Access Ram
;	tempBank1
;	endc	

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

;Example 1:

	movlb	1		;Set bank select register to 1
	movlw	1		;Place 1 in wreg
	movwf	CNT, 1		;Using bank select, copy wreg value into CNT
	decf	CNT, 1, 1	;Using bank select, decrement CNT, place result in CNT

	decf	CNT, 1, 1	;Decrement CNT again to test rollover from 0x00 to 0xff

;Example 2:

	lfsr	0, 0x1c2	;Load FSR0 with 0x1c2
	movlw	1		;Place 1 in wreg
	movwf	INDF0		;Copy wreg value to location pointed to by FSR0
	decf	INDF0, 1, 1	;Decrement value in location pointed to by FSR0,
				;place result in location pointed to by FSR0. Ignores
				;banked addressing since register argument is an INDF register

;Check writing to wreg
	decf	INDF0, 0
					
stop	goto	stop

	end






















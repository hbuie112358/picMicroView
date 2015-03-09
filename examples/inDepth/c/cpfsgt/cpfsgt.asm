;;;;;;; cpfsgt.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of cpfsgt instruction. Cpfsgt skips next instruction
;if f is greater than wreg.
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
	FLAG
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

;Example 1:
;Case 1, 2:

		movlb	0x01		;Set BSR to 0x01 for banked addressing
		movlw	0x5a		;Place 0x5a in wreg
		movwf	FLAG, 1		;Copy wreg value into FLAG
HERE		cpfsgt	FLAG, 1		;Skips next instruction if contents of f > contents of wreg
NGT		goto	PROCESS_CODE
GT		goto	stop

PROCESS_CODE	nop			;Came to PROCESS_CODE since wreg, f, were not equal
		movlw	0xa5		;Place 0xa5 in wreg
		movwf	FLAG, 1		;Copy contents of wreg into FLAG
		movlw	0x5a		;Place 0x5a in wreg
		goto	HERE
	

stop	goto		stop

	end


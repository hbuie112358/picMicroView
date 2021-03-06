;;;;;;; sublw.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of sublw instruction. Sublw subtracts the wreg
;from the literal k (two's complement method). This instruction is tested 
;according to the examples in the pic18 user manual.
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

OFFSET 	equ	0x10		;Declare constant OFFSET to have value 0x10
	movlw		0x37	;Place 0x37 in wreg
	sublw		OFFSET	;Subtract wreg value from constant OFFSET
				;0x10 - 0x37 = 0xd9, status register N bit is set


;Example 2:
;Case 1:

	movlw		0x01	;Place 0x01 in wreg
	sublw		0x02	;Subtract wreg value 1 from 2
				;0x02 - 0x01 = 0x01, status register C, DC bits are set

;Case 2:

	movlw		0x02	;Place 0x02 in wreg
	sublw		0x02	;Subtract wreg value 2 from 2
				;0x02 - 0x02 = 0x00, status register C, DC, Z bits are set

;Case 3:

	movlw		0x03	;Place 0x03 in wreg
	sublw		0x02	;Subtract wreg value 3 from 2
				;0x02 - 0x03 = 0xff, status register N bit is set
	
stop	goto		stop

	end


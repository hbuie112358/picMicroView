;;;;;;; bsf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of bsf instruction according to pic18 user
;manual examples. Bsf sets bit b in register f.
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

	cblock  0x100           ;Bank 1 of Access RAM
	FLAG_REG
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
		movlb		1		;Place 1 in BSR for bank 1
		movlw		10		;Place 10 in wreg
		movwf		FLAG_REG, 1	;Copy wreg value to FLAG_REG
		bsf		FLAG_REG, 7	;Set bit 7 of FLAG_REG

;Example 2:
		movlw		0x20		;Place 0x20 in wreg
		lfsr		0, 0x0c2	;Place 0x0c2 in FSR0H:FSR0L
		movwf		INDF0		;Copy wreg into register pointed to by FSR0
		movlw		0x17		;Place 0x17 in wreg
		bsf		INDF0, 3	;Set bit 3 in register pointed to 
						;by FSR0H:FSR0L


stop		goto		stop

		end























;;;;;;;addwfc.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
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

	movlw	0x17
	movwf	temp		;Store 0x17 at address 0x000
	movlw	0xc2
	addwfc	temp, 0		;Add WREG, C bit, and temp, 
				;place result (0xd9) in WREG (no carry)

;Cause carry bit to be set:

	movlw	0xfe
	movwf	temp
	movlw	0xad
	addwfc	temp, 0		;Set carry bit (status reg, bit 0)

;Test add with carry:

	movwf	temp		
	movlw	0x02
	addwfc	temp, 0		;Add WREG, C bit, and temp (0x02 + 1 + 0xab), 
				;place result (0xae) in WREG (clears carry bit)

stop	goto		stop

	end






















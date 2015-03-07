;;;;;;; clrf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of clrf instruction. Clrf clears the contents
;of register f, then sets the Z bit
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

	movlb	1		;Select bank 1 of data memory
	movlw	0x5a		;Place 0x5a in wreg
	movwf	FLAG_REG, 1	;Copy wreg value to FLAG_REG
	clrf	FLAG_REG, 1	;Using banked addressing, clear register FLAG_REG


;Example 2:

	lfsr	0, 0x0c2		;Place 0x0c2 in FSR
	movlw	0xaa		;Place 0xaa in wreg
	movwf	INDF0, 1	;Copy wreg value into address pointed to by FSR0,
				;movwf ignores banked addressing portion of instruction	
	clrf	INDF0, 1	;Clear contents of register pointed to by FSR0,
				;clrf again ignores banked addressing portion of instruction.
	

stop	goto		stop

	end


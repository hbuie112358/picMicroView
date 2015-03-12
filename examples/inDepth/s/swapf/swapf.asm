;;;;;;; swapf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of swapf according to examples in the PIC18 user
;manual. Swapf swaps the upper and lower nibbles of register f.
;
;;;;;;; Program hierarchy ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Mainline
;  
;
;;;;;;; Assembler directives ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

        list  P=PIC18F452, F=INHX32, C=160, N=0, ST=OFF, MM=OFF, R=DEC, X=ON
        #include p18f452.inc

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

Mainline
;Example 1:
	
	movlb	0x01		;Select bank 1 for banked addressing
	movlw	0xa5		;Place 0xa5 in wreg
	movwf	REG1, 1		;Copy wreg to REG1
	swapf	REG1, 0, 1	;Swap high and low nibble of REG1, result in wreg

;Example 2:
	
	lfsr	0, 0x5c2	;Place 0x5c2 in FSR0
	movlw	0x20		;Place 0x20 in wreg
	movwf	INDF0		;Copy wreg to register at address pointed to by FSR0
	swapf	INDF0, 1, 1	;Swap high and low nibble of register pointed to by 
				;FSR0, result to register pointed to by FSR0

;Example 3:

	swapf	REG1, 1, 1	;Swap high and low nibble of REG1, result in REG1

stop	goto	stop

	end



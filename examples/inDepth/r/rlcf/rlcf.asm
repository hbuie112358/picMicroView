;;;;;;; rlcf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of rlcf according to examples in the PIC18 user
;manual. Rlcf rotates the contents of register f through the carry bit.
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
	movlw	0xe6		;Place 0xe6 in wreg
	movwf	REG1, 1		;Copy wreg to REG1
	rlcf	REG1, 0, 1	;Using banked addressing, rotate left through carry
				;REG1, place result in wreg

;Example 2:
;Case 1:
	
	lfsr	0, 0x0c2	;Place 0x0c2 in FSR0
	movlw	0x3a		;Place 0x3a in wreg
	movwf	INDF0		;Copy wreg to register at address pointed to by FSR0
	bsf	STATUS, 0	;Set carry bit in status register
	rlcf	INDF0, 1, 1	;Rotate left through carry value in register pointed to
				;by FSR0, place result back in register, carry bit is 
				;loaded into bit 0

;Case 2:
	
	movlw	0xb9		;Place 0xb9 into wreg
	movwf	INDF0		;Copy wreg to register at address pointed to by FSR0
	rlcf	INDF0, 1, 1	;Rotate left through carry value in register pointed to
				;by FSR0, place result back in register, carry bit is 
				;loaded into bit 0

stop	goto	stop

	end



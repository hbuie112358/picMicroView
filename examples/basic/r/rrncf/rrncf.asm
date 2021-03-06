;;;;;;; rrncf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of rrcf according to examples in the PIC18 user
;manual. Rrncf rotates the contents of register f right, bit 0 goes to bit 7.
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
	
	movlw	0xe6		;Place 0xe6 in wreg
	movwf	REG1, 1		;Copy wreg to REG1
	rrncf	REG1, 0, 1	;Rotate right REG1, place result in wreg

;Example 2:
;Case 1:
	
	movlw	0x3a		;Place 0x3a in wreg
	movwf	REG1		;Copy wreg to register at address pointed to by FSR0
	rrncf	REG1, 1		;Rotate right value in REG1, place result back in register,
				;bit 0 is loaded into bit 7 

;Case 2:
	
	movlw	0x39		;Place 0x39 into wreg
	movwf	REG1		;Copy wreg to register at address pointed to by FSR0
	rrncf	REG1, 1		;Rotate right value in REG1, place result back in register, 
				;bit 0 is loaded into bit 7

stop	goto	stop

	end



;;;;;;; setf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of setf according to examples in the PIC18 user
;manual. Setf sets every bit in register f to 1. That is, it loads register f
;with 0xff.
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
	
	movlb	0x01		;Select bank 1 for banked addressing
	movlw	0x5a		;Place 0xe6 in wreg
	movwf	FLAG_REG, 1	;Copy wreg to FLAG_REG
	setf	FLAG_REG, 1	;Using banked addressing, set all bits in FLAG_REG

;Example 2:
	
	lfsr	0, 0x4c2	;Place 0x4c2 in FSR0
	movlw	0xaa		;Place 0xaa in wreg
	movwf	INDF0		;Copy wreg to register at address pointed to by FSR0
	setf	INDF0, 1	;Set all bits in register pointed to by FSR0, place 
				;result back in register.

stop	goto	stop

	end



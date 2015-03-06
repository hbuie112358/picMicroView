;;;;;;; bcf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of bcf according to examples in the PIC18 user
;manual. Bcf clears the specified bit in the specified register.
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
	MYREG	
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

	movlb	2		;Specify bank 2 (addresses starting at 0x200)
	movlw	0xc7		;Place 0xc7 in wreg
	movwf	MYREG, 1	;Copy wreg value int MYREG at address 0x200
	bcf	MYREG, 7, 1	;Clear bit 7 of MYREG

;Example 2:
	
	lfsr	1, 0x3c2	;Load FSR with 0x3c2
	movlw	0x2f		;Place 0x2f in wreg
	movwf	INDF1		;Copy wreg to register pointed to by FSR1
	bcf	INDF1, 3, 0	;Clear bit 3 in register pointed to by FSR1


stop		goto		stop

		end














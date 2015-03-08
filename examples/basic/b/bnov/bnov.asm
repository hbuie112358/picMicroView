;;;;;;; bnov.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests the bnov instruction according to the examples in the PIC18
;user manual. Bnov branches if status register OV bit is 0.
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
;Case 1:

		bsf	STATUS, 3	;Set overflow bit in status register
HERE		bnov	OV_CODE		;If overflow bit is not set, branch to label OV_CODE
NOT_OV		nop			;
		nop			;
		goto	MORE_CODE	;Since overflow bit was set, execute this MORE_CODE
OV_CODE	nop
		nop
		goto 	stop
MORE_CODE	bcf	STATUS, 3	;Clear overflow bit in status register
		goto	HERE		;Go back to label HERE, this time with overflow bit not set


stop	goto	stop

	end



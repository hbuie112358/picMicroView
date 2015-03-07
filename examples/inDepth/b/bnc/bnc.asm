;;;;;;; bnc.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of bnc according to examples in the PIC18 user
;manual. Bnc branches if status register N bit is 0.
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

		bsf	STATUS, 4	;Set negative bit in status register
HERE		bnc	NC_CODE		;If negative bit is not set, branch to label N_CODE
NOT_N		nop			;
		nop			;
		goto	MORE_CODE	;Since negative bit was set, execute this MORE_CODE
NC_CODE		btg	STATUS, 4	;Sets negative bit
		nop
		goto 	HERE2
MORE_CODE	bcf	STATUS, 4	;Clear negative bit in status register
		goto	HERE		;Go back to label HERE, this time with negative bit clear

;Examples 2, 3:
;Cases 1, 2:
	
OFFSET 	equ	10			;Set constant OFFSET to 10
HERE2		bnc	$ + OFFSET	;If negative bit clear, branch to HERE2 + OFFSET
		btg	STATUS, 4	;Toggle negative bit in status register
NO_N		nop
		goto	HERE2		;Go to label HERE2
		btg	STATUS, 4	;Branches here after $ + OFFSET since OFFSET is 10
					;and negative bit is clear.
		nop			
		nop			
		nop
		nop
		btg	STATUS, 4
HERE3		bnc	$ - OFFSET	;If carry bit set, branch to HERE3 - OFFSET


stop	goto	stop

	end



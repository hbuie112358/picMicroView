;;;;;;; bov.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests the bov instruction according to the examples in the PIC18
;user manual. Bov branches if status register OV bit is 1.
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

		bcf	STATUS, 3	;Clear overflow bit in status register
HERE		bov	OV_CODE		;If overflow bit is set, branch to label OV_CODE
NOT_OV		nop			;
		nop			;
		goto	MORE_CODE	;Since overflow bit was not set, execute MORE_CODE
OV_CODE		nop
		nop
		btg	STATUS, 3
		goto 	HERE2
MORE_CODE	bsf	STATUS, 3	;Set overflow bit in status register
		goto	HERE		;Go back to label HERE, this time with overflow bit set

;Examples 2, 3:
;Cases 1, 2:
	
OFFSET 	equ	10			;Set constant OFFSET to 10
HERE2		bov	$ + OFFSET	;If overflow bit set, branch to HERE2 + OFFSET
		btg	STATUS, 3	;Toggle overflow bit in status register
NO_N		nop
		goto	HERE2		;Go to label HERE2
		btg	STATUS, 3	;Branches here after $ + OFFSET since OFFSET is 10
					;and overflow bit is set.
		nop			
		nop			
		nop
		nop
		btg	STATUS, 3
HERE3		bov	$ - OFFSET	;If overflow bit set, branch to HERE3 - OFFSET


stop	goto	stop

	end



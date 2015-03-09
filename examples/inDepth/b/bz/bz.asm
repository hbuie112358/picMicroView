;;;;;;; bz.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests the bz instruction according to the examples in the
;pic18 user manual. Bz branches to the specified label if the status register
;Z bit is set.
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

		bcf	STATUS, 2	;Clear zero bit in status register
HERE		bz	Z_CODE		;If zero bit is set, branch to label Z_CODE
NOT_Z		nop			;
		nop			;
		goto	MORE_CODE	;Since zero bit was not set, execute this MORE_CODE
Z_CODE		btg	STATUS, 2
		nop
		goto 	HERE2
MORE_CODE	bsf	STATUS, 2	;Set zero bit in status register
		goto	HERE		;Go back to label HERE, this time with zero bit set

;Examples 2, 3:
;Cases 1, 2:
	
OFFSET 	equ	10			;Set constant OFFSET to 10
HERE2		bz	$ + OFFSET	;If zero bit set, branch to HERE2 + OFFSET
		btg	STATUS, 2	;Toggle zero bit in status register
NO_Z		nop
		goto	HERE2		;Go to label HERE2
		btg	STATUS, 2	;Branches here after $ + OFFSET since OFFSET is 10
					;and zero bit is set.
		nop			
		nop			
		nop
		nop
		btg	STATUS, 2
HERE3		bz	$ - OFFSET	;If zero bit set, branch to HERE3 - OFFSET

stop		goto		stop

	end






















;;;;;;; bc.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of bc according to examples in the PIC18 user
;manual. Bc branches if the status register carry bit is 1.
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

		bcf	STATUS, 0	;Clear carry bit in status register
HERE1		bc	C_CODE		;If carry bit is set, branch to label C_CODE
NOT_C		nop			;
		nop			;
		goto	MORE_CODE	;Since carry bit was not set, execute this MORE_CODE
C_CODE		nop
		nop
		bcf	STATUS, 0	;Clear carry bit in status register
		goto 	HERE2
MORE_CODE	bsf	STATUS, 0	;Set carry bit in status register
		goto	HERE1		;Go back to label HERE1, this time with carry bit set

;Examples 2, 3:
;Cases 1, 2:
	
OFFSET 	equ	10			;Set constant OFFSET to 10
HERE2		bc	$ + OFFSET	;If carry bit set, branch to HERE2 + OFFSET
		btg	STATUS, 0	;Toggle carry bit in status register
NO_C		nop
		goto	HERE2		;Go to label HERE2
		btg	STATUS, 0	;Branches here after $ + OFFSET since OFFSET is 10
					;and carry bit is set.
		nop			
		nop			
		nop
		nop
		btg	STATUS, 0
HERE3		bc	$ - OFFSET	;If carry bit set, branch to HERE3 - OFFSET


stop	goto	stop

	end



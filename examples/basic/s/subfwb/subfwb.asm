;;;;;;; subfwb.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of subfwb instruction. Subfwb subtracts register
;f and carry flag from wreg (two's complement method).
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

	bcf	STATUS, 0	;Clear status register carry bit (borrow bit)
	movlw	0x37		;Place 0x37 in wreg
	movwf	MYREG		;Copy wreg to MYREG
	movlw	0x10		;Place 0x10 in wreg
	subfwb	MYREG, 1, 1	;Subtract MYREG and borrow bit from wreg,
				;MYREG: 0xa8, N bit is set

;Example 2:
;Case 1:

	bsf	STATUS, 0	;Set status register carry (borrow) bit 
	movlw	0x03		;Place 0x03 in wreg
	movwf	MYREG		;Copy wreg to MYREG
	movlw	0x02		;Place 0x02 in wreg
	subfwb	MYREG, 1, 1	;Subtract MYREG and borrow bit from wreg,
				;MYREG: 0xff, N bit is set

;Case 2:

	bsf	STATUS, 0	;Set status register carry (borrow) bit 
	movlw	0x02		;Place 0x02 in wreg
	movwf	MYREG		;Copy wreg to MYREG
	movlw	0x02		;Place 0x02 in wreg
	subfwb	MYREG, 1, 1	;Subtract MYREG and borrow bit from wreg,
				;MYREG: 0x00, status register Z, C, DC bits are set

;Case 3:

	bsf	STATUS, 0	;Set status register carry (borrow) bit 
	movlw	0x01		;Place 0x01 in wreg
	movwf	MYREG		;Copy wreg to MYREG
	movlw	0x03		;Place 0x03 in wreg
	subfwb	MYREG, 1, 1	;Subtract MYREG and borrow bit from wreg,
				;MYREG: 0x02, status register C and DC bits are set
	

stop	goto		stop

	end

